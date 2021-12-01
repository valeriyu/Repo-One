package com.skillbox.github.ui.repository_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.github.AppRepository
import com.skillbox.github.ui.current_user.CurrentUser
import com.skillbox.github.ui.current_user.UpdReqBody
import com.skillbox.github.utils.SingleLiveEvent
import kotlinx.coroutines.*

class RepositoriesViewModel : ViewModel() {

    private val repository = AppRepository()

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable)
        Log.e("ErrorCancelFragment", "error from CoroutineExceptionHandler", throwable)
        launchCoroutineAfterError()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + errorHandler)

    private val piplFollofsLiveData = MutableLiveData<List<CurrentUser>>()
    val piplFollofs: LiveData<List<CurrentUser>>
        get() = piplFollofsLiveData

    private val toastLiveEvent = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = toastLiveEvent

    private val reposListLiveData = MutableLiveData<List<Repository>>(emptyList())
    val reposList: LiveData<List<Repository>>
        get() = reposListLiveData

    private val currUserLiveData = MutableLiveData<CurrentUser>()
    val currUser: LiveData<CurrentUser>
        get() = currUserLiveData

    private val repoLiveData = MutableLiveData<Repository>()
    val repo: LiveData<Repository>
        get() = repoLiveData

    private val isLoadingLiveData = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    private val isStarredLiveData = MutableLiveData<Boolean>()
    val isStarred: LiveData<Boolean>
        get() = isStarredLiveData


    private fun launchCoroutineAfterError() {
        scope.launch {
            Log.d("_Coroutines_", "coroutine launched after error")
        }
    }

    private fun onError(throwable: Throwable) {
        toastLiveEvent.postValue(throwable.message)
        isLoadingLiveData.postValue(false)
        currUserLiveData.postValue(null)
        reposListLiveData.postValue(emptyList())
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun getPubRepositories() {
        isLoadingLiveData.postValue(true)
        coroutineScope.launch {
            try {
                var list = repository.getPubRepositories()
                reposListLiveData.postValue(list)
                isLoadingLiveData.postValue(false)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    fun checkRepositoryIsStarred(owner: String, repo: String) {
        viewModelScope.launch {
            try {
                //isLoadingLiveData.postValue(true)
                if (repository.checkRepositoryIsStarred(owner, repo)
                //if (repository.checkRepositoryIsStarredSuspend(owner, repo)
                ) {
                    isStarredLiveData.postValue(true)
                } else {
                    isStarredLiveData.postValue(false)
                }
                isLoadingLiveData.postValue(false)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    fun getCurrentUserInfo() {
        var user: CurrentUser? = null
        var listPiplFollofs: List<CurrentUser> = emptyList()
        isLoadingLiveData.postValue(true)
        scope.launch {
            var dUser = async {
                repository.getCurrentUserInfo()
            }

            var dListPiplFollofs = async {
                repository.peopleWhoUserFollows()
            }

            user = dUser.await()
            listPiplFollofs = dListPiplFollofs.await()

            currUserLiveData.postValue(user)
            piplFollofsLiveData.postValue(listPiplFollofs)
            isLoadingLiveData.postValue(false)
        }
    }

    fun setStar(
        owner: String, repo: String, star: Boolean
    ) {
        isLoadingLiveData.postValue(true)
        var status = 0
        viewModelScope.launch {
            try {

                status = repository.setStar(owner, repo, star)
            } catch (t: Throwable) {
                onError(t)
            }

            if (status == 204 && star) {
                isStarredLiveData.postValue(true)
                isLoadingLiveData.postValue(false)
            }
            if (status == 204 && !star) {
                isStarredLiveData.postValue(false)
                isLoadingLiveData.postValue(false)
            }
        }
    }

    fun updateCurrentUserInfo(body: UpdReqBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoadingLiveData.postValue(true)
                var res = repository.updateCurrentUserInfo(body)
                if (res == 200) {
                    getCurrentUserInfo()
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    fun getRepo(
        owner: String, repo: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoadingLiveData.postValue(true)
                repoLiveData.postValue(repository.getRepo(owner, repo))
                isLoadingLiveData.postValue(false)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}