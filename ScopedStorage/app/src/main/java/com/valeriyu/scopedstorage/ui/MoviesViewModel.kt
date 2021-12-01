package com.valeriyu.scopedstorage

import android.app.Application
import android.app.RecoverableSecurityException
import android.app.RemoteAction
import android.content.IntentSender
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class MoviesViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val moviesRepository = MoviesRepository(app)

    private val permissionsGrantedMutableLiveData = MutableLiveData(true)
    private val toastSingleLiveEvent = SingleLiveEventK<String>("")
    private val moviesMutableLiveData = MutableLiveData<List<Movie>>()
    private val recoverableActionMutableLiveData = MutableLiveData<RemoteAction>()

    private var isObservingStarted: Boolean = false
    private var pendingDeleteId: Long? = null

    private val _actions = MutableLiveData<IntentSender>()
    val actions: LiveData<IntentSender>
    get() = _actions

    val toastLiveData: LiveData<String>
        get() = toastSingleLiveEvent

    val moviesLiveData: LiveData<List<Movie>>
        get() = moviesMutableLiveData

    val permissionsGrantedLiveData: LiveData<Boolean>
        get() = permissionsGrantedMutableLiveData

    val recoverableActionLiveData: LiveData<RemoteAction>
        get() = recoverableActionMutableLiveData

    override fun onCleared() {
        super.onCleared()
        moviesRepository.unregisterObserver()
    }

    fun addToFavorites(movies: List<Movie>, state: Boolean){
        try {
        _actions.postValue(moviesRepository.addToFavorites(movies, state))
        } catch (t: Throwable) {
            Timber.e(t)
            toastSingleLiveEvent.postValue(t.message)
        }
    }
    fun addToTrashed(movies: List<Movie>, state: Boolean) {
        try {
        _actions.postValue(moviesRepository.addToTrash(movies, state))
        } catch (t: Throwable) {
            Timber.e(t)
            toastSingleLiveEvent.postValue(t.message)
        }
    }

    fun updatePermissionState(isGranted: Boolean) {
        if(isGranted) {
            permissionsGranted()
        } else {
            permissionsDenied()
        }
    }

    fun permissionsGranted() {
        loadMovies()
        if(isObservingStarted.not()) {
            moviesRepository.observeImages { loadMovies() }
            isObservingStarted = true
        }
        permissionsGrantedMutableLiveData.postValue(true)
    }

    fun permissionsDenied() {
        permissionsGrantedMutableLiveData.postValue(false)
    }

    fun deleteImage(id: Long) {
        viewModelScope.launch {
            try {
                moviesRepository.deleteMovie(id)
                pendingDeleteId = null
            } catch (t: Throwable) {
                Timber.e(t)
                if(haveQ() && t is RecoverableSecurityException) {
                    pendingDeleteId = id
                    recoverableActionMutableLiveData.postValue(t.userAction)
                } else {
                    toastSingleLiveEvent.postValue("Ошибка при удалении файла")
                }
            }
        }
    }

    fun confirmDelete() {
        pendingDeleteId?.let {
            deleteImage(it)
        }
    }

    fun declineDelete() {
        pendingDeleteId = null
    }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                val images = moviesRepository.getMovies()
                moviesMutableLiveData.postValue(images)
            } catch (t: Throwable) {
                Timber.e(t)
                moviesMutableLiveData.postValue(emptyList())
                toastSingleLiveEvent.postValue("Ошибка при загрузке списка")
            }
        }
    }
}