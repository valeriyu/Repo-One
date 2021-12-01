package com.valeriyu.flow.ui

import android.app.Application
import androidx.lifecycle.*
import com.valeriyu.flow.data.MovieRepository
import com.valeriyu.flow.models.MovieType
import com.valeriyu.flow.models.Movies
import com.valeriyu.notifications.SingleLiveEventK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.Call
import timber.log.Timber

@InternalCoroutinesApi
class MoviesViewModel
    (
    application: Application
) : AndroidViewModel(application) {

    private lateinit var job: Job
    private var call: Call? = null
    private val movieRepository = MovieRepository(application)

    private var _moviesListFlow = movieRepository.observeMovies
    var moviesListFlow = emptyList<List<Movies>>().asFlow()
        get() = _moviesListFlow

    private val isLoadingLiveData = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData


    private val moviesLiveData = movieRepository.moviesList.asLiveData()
    val movies: LiveData<List<Movies>>
        get() = moviesLiveData

    private val errLiveData = SingleLiveEventK<String>("")

    //private val errLiveData = MutableLiveData<String>()
    val error: SingleLiveEventK<String>
        get() = errLiveData

    private fun onError(t: Throwable) {
        isLoadingLiveData.postValue(false)
        errLiveData.postValue(t.message)
        Timber.e(t, t.message)
    }

    override fun onCleared() {
        super.onCleared()
        call = null
        job.cancel()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    suspend fun bind(queryFlow: Flow<String>, movieTypeFlow: Flow<MovieType>) {

        job = combine(
            queryFlow.onStart { emit("") },
            movieTypeFlow.onStart { emit(MovieType.ALL) },
            { query, movieType -> query to movieType }
        )
            .debounce(200)
            .distinctUntilChanged()
            .mapLatest { (query, movieType) ->
                var _movieType = if (movieType != MovieType.ALL) {
                    movieType.toString().toLowerCase()
                } else ""
                isLoadingLiveData.postValue(true)
                movieRepository.searchMovies(query, _movieType)
                isLoadingLiveData.postValue(false)
            }
            .catch {
                isLoadingLiveData.postValue(false)
                errLiveData.postValue("Что то пошло не так !!!")
                job.cancel()
                bind(queryFlow, movieTypeFlow)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
        job.start()
    }
}
