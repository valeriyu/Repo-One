package com.valeriyu.notifications.ui

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.valeriyu.notifications.Message
import com.valeriyu.notifications.MsgWihUserName
import com.valeriyu.notifications.NotificationsRepository
import com.valeriyu.notifications.SingleLiveEventK
import com.valeriyu.notifications.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class NotificationsViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val notificationsRepository = NotificationsRepository(app)

    private val errorLiveEvent = SingleLiveEventK<String>("")
    val errorLiveData: SingleLiveEventK<String>
        get() = errorLiveEvent


    var moviesListFlow = emptyList<List<MsgWihUserName>>().asFlow()
        get() = notificationsRepository.messagesListFlow


    /*private val progressLiveEvent = SingleLiveEventK<Int>(0)
    val progressEvent: SingleLiveEventK<Int>
        get() = progressLiveEvent

    private val toastSingleLiveEvent = SingleLiveEventK<String>("")
    val toastLiveData: LiveData<String>
        get() = toastSingleLiveEvent


    private val saveSuccessSingleLiveEvent = SingleLiveEvent<Unit>()
    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessSingleLiveEvent*/

    private val loadingMutableLiveData = MutableLiveData<Boolean>(false)
    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable)
    }

    //==================================================================================================

    private fun onError(t: Throwable) {
        errorLiveEvent.postValue(t.message)
        loadingMutableLiveData.postValue(false)
        Timber.e(t, t.message)
    }

    private val coroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default + errorHandler)

    fun downloadFile(name: String, url: String) {
        coroutineScope.launch {
            loadingMutableLiveData.postValue(true)
            notificationsRepository.downloadFileWithProgress(name, url)
        }
    }
}