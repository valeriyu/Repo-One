package com.valeriyu.roomdao.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.valeriyu.roomdao.MessangerRepository
import com.valeriyu.roomdao.SingleLiveEventK
import com.valeriyu.roomdao.data.db.models.attachments.Attachments
import com.valeriyu.roomdao.data.db.models.attachments.MessagesWithAttachments
import com.valeriyu.roomdao.data.db.models.messages.Messages
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.utils.SingleLiveEvent
import kotlinx.coroutines.*
import timber.log.Timber


class MessangerViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val userRepository = MessangerRepository(application)

    val usersObservable: LiveData<List<User>>
        get() = userRepository.userDao.getUsersObservable()

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable)
    }

    private val coroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default + errorHandler)

    private val isLoadingLiveData = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    private val errorLiveEvent = SingleLiveEventK<String>("")
    val errorLiveData: SingleLiveEventK<String>
        get() = errorLiveEvent

    private val saveSuccessLiveEvent = SingleLiveEvent<Unit>()
    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessLiveEvent

    private val usersWithPropMutableLiveData = MutableLiveData<List<User?>>()
    val usersWithProp: LiveData<List<User?>>
        get() = usersWithPropMutableLiveData

    private var messagesWithAttachmentsLiveData = MutableLiveData<List<MessagesWithAttachments>>()
    val messagesWithAttachments: LiveData<List<MessagesWithAttachments>>
        get() = messagesWithAttachmentsLiveData

//==================================================================================================

    private fun onError(t: Throwable) {
        errorLiveEvent.postValue(t.message)
        isLoadingLiveData.postValue(false)
        Timber.e(t, t.message)
    }

    fun loadMessagesList(userId: Long) {
        coroutineScope.launch {
            var newList = userRepository.getUserMessages(userId)
            messagesWithAttachmentsLiveData.postValue(newList)
        }
    }

    fun loadUsersList() {
        viewModelScope.launch {
            var newList = userRepository.getAllUsersWithProperties()
            //usersWithPropMutableLiveData.postValue(newList)
        }
    }

    fun getUserWhithProp(id: Long) {
        viewModelScope.launch {
            var uwp: User? = userRepository.getUserWithPropertiesById(id)
            if (uwp == null) {
                usersWithPropMutableLiveData.postValue(emptyList())
            } else {
                usersWithPropMutableLiveData.postValue(listOf(uwp))
            }
            isLoadingLiveData.postValue(false)
        }
    }

    fun removeUser(uwp: User) {
        viewModelScope.launch {
            userRepository.removeUser(uwp.id)
            loadUsersList()
        }
    }

    fun deleteAttachment(attach: Attachments) {
        coroutineScope.launch {
            userRepository.deleteAttachment(attach)
        }
    }

    fun initDB() {
        viewModelScope.launch {
            userRepository.initDB()
        }
    }

   fun createMessage(sId: Long, rId: Long) {
        coroutineScope.launch {
            userRepository.addMessage(sId, rId)
        }
    }

    suspend fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.coroutineContext.cancel()
    }

    fun saveUserWithPropirties(
        uwp: User
    ) {
        viewModelScope.launch {
            isLoadingLiveData.postValue(true)
            try {
                userRepository.saveUserWithPropierties(uwp)
                saveSuccessLiveEvent.postValue(Unit)
                isLoadingLiveData.postValue(false)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    fun deleteMessageById(id: Long) {
        coroutineScope.launch {
            isLoadingLiveData.postValue(true)
            userRepository.deleteMessageById(id)
            isLoadingLiveData.postValue(false)
        }
    }
}
