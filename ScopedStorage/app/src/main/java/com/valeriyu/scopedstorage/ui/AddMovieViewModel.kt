package com.valeriyu.scopedstorage

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.valeriyu.scopedstorage.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class AddMovieViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val moviesRepository = MoviesRepository(app)

    private val toastSingleLiveEvent = SingleLiveEvent<Int>()
    private val saveSuccessSingleLiveEvent = SingleLiveEvent<Unit>()
    private val loadingMutableLiveData = MutableLiveData<Boolean>(false)

    val toastLiveData: LiveData<Int>
        get() = toastSingleLiveEvent

    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData

    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessSingleLiveEvent

    fun saveMovie(name: String, url: String) {
        viewModelScope.launch {
            loadingMutableLiveData.postValue(true)
            try {
                moviesRepository.saveMovie(name, url)
                saveSuccessSingleLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                Timber.e(t)
                toastSingleLiveEvent.postValue(R.string.movie_add_error)
            } finally {
                loadingMutableLiveData.postValue(false)
            }
        }
    }

    fun saveFile(url: String, uri: Uri) {
        viewModelScope.launch {
            loadingMutableLiveData.postValue(true)
            try {
                moviesRepository.downloadFile(url, uri)
                saveSuccessSingleLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                Timber.e(t)
                toastSingleLiveEvent.postValue(R.string.file_add_error)
            } finally {
                loadingMutableLiveData.postValue(false)
            }
        }
    }


}