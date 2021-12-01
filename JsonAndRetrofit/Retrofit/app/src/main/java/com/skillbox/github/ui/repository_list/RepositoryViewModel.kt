package com.skillbox.github.ui.repository_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.AppRepository
import com.skillbox.github.ui.current_user.CurrentUser
import com.skillbox.github.ui.current_user.UpdReqBody
import com.skillbox.github.utils.SingleLiveEvent

class RepositoriesViewModel : ViewModel() {

    private val repository = AppRepository()

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

    private  fun onError(throwable: Throwable){
        toastLiveEvent.postValue(throwable.message)
        isLoadingLiveData.postValue(false)
        currUserLiveData.postValue(null)
        repoLiveData.postValue(null)
    }

    fun getPubRepositories() {
        isLoadingLiveData.postValue(true)
        repository.getPubRepositories(
            onComplete = { list ->
                isLoadingLiveData.postValue(false)
                reposListLiveData.postValue(list as List<Repository>?)
            },
            onError = { throwable ->
                onError(throwable)
            }
        )
    }


    fun updateCurrentUserInfo(body: UpdReqBody) {
        isLoadingLiveData.postValue(true)
        repository.updateCurrentUserInfo(
            body,
            onComplete = { user ->
                isLoadingLiveData.postValue(false)
                currUserLiveData.postValue(user as CurrentUser)
            },
            onError = { throwable ->
                onError(throwable)
            }
        )
    }


    fun getCurrentUserInfo() {
        isLoadingLiveData.postValue(true)
        repository.getCurrentUserInfo(
            onComplete = { user ->
                isLoadingLiveData.postValue(false)
                currUserLiveData.postValue(user as CurrentUser)
            },
            onError = { throwable ->
                onError(throwable)
            }
        )
    }

    fun checkRepositoryIsStarred(
        owner: String, repo: String
    ) {
        isLoadingLiveData.postValue(true)
        repository.checkRepositoryIsStarred(owner, repo,
            onComplete = { status ->
                isLoadingLiveData.postValue(false)
                if (status == 204) {
                    isStarredLiveData.postValue(true)
                } else {
                    isStarredLiveData.postValue(false)
                }
            },
            onError = { throwable ->
                onError(throwable)
            }
        )
    }

    fun getRepo(
        owner: String, repo: String
    ) {
        isLoadingLiveData.postValue(true)
        repository.getRepo(owner, repo,
            onComplete = { repo ->
                isLoadingLiveData.postValue(false)
                repoLiveData.postValue(repo as Repository)
            },
            onError = { throwable ->
                onError(throwable)
            }
        )
    }


    fun setStar(
        owner: String, repo: String, star: Boolean
    ) {
        isLoadingLiveData.postValue(true)
        repository.setStar(owner, repo, star,
            onComplete = { status ->
                if (status == 204 && star) {
                    isStarredLiveData.postValue(true)
                    isLoadingLiveData.postValue(false)
                }
                if (status == 204 && !star) {
                    isStarredLiveData.postValue(false)
                    isLoadingLiveData.postValue(false)
                }
            },
            onError = { throwable ->
                onError(throwable)
            }
        )
    }
}