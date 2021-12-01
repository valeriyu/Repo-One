package com.valeriyu.roomdao

import android.app.Application
import androidx.lifecycle.*
import com.valeriyu.roomdao.app.Database
import com.valeriyu.roomdao.data.db.models.attachments.Attachments
import com.valeriyu.roomdao.data.db.models.attachments.MessagesWithAttachments
import com.valeriyu.roomdao.data.db.models.messages.Messages
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users.UsersWithProperties
import com.valeriyu.roomdao.utils.SingleLiveEvent
import kotlinx.coroutines.*
import timber.log.Timber


class MessangerViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val userRepository = MessangerRepository(application)
    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable)
    }

    private val coroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default + errorHandler)

    //val msgLiveData: LiveData<List<Messages>> = Database.instance.messagesDao().getLastMessage()

    private val isLoadingLiveData = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    private val errorLiveEvent = SingleLiveEventK<String>("")
    val errorLiveData: SingleLiveEventK<String>
        get() = errorLiveEvent

    private val saveSuccessLiveEvent = SingleLiveEvent<Unit>()
    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessLiveEvent

    private val usersWithPropMutableLiveData = MutableLiveData<List<UsersWithProperties?>>()
    val usersWithProp: LiveData<List<UsersWithProperties?>>
        get() = usersWithPropMutableLiveData

    /* private val messagesWithAttachmentsLiveData = MutableLiveData<List<MessagesWithAttachments>>()
     val messagesWithAttachments: LiveData<List<MessagesWithAttachments>>
         get() = messagesWithAttachmentsLiveData*/


    private var messagesWithAttachmentsLiveData = MutableLiveData<List<MessagesWithAttachments>>()
    val messagesWithAttachments: LiveData<List<MessagesWithAttachments>>
        get() = messagesWithAttachmentsLiveData

//==================================================================================================

    /* fun init(id: Long) {
         viewModelScope.launch {
             try {
                 val user = userRepository.getUserById(id)
                 if (user != null) usersWithPropMutableLiveData.postValue(user)
             } catch (t: Throwable) {
                 Timber.e(t)
             }
         }
     }*/


    private fun onError(t: Throwable) {
        errorLiveEvent.postValue(t.message)
        isLoadingLiveData.postValue(false)
        Timber.e(t, t.message)
    }

    fun loadMessagesList(userId: Long) {
        coroutineScope.launch {
            //var newList = userRepository._messagesDao.getDistinctMessageById(userId).value
            var newList = userRepository.getUserMessages(userId)
            messagesWithAttachmentsLiveData.postValue(newList)
        }
    }

    fun loadUsersList() {
        viewModelScope.launch {
            usersWithPropMutableLiveData.postValue(userRepository.getAllUsersWithProperties())
        }
    }

    fun getUserWhithProp(id: Long) {
        viewModelScope.launch {
            var uwp: UsersWithProperties? = userRepository.getUserWithPropertiesById(id)
            if (uwp == null) {
                usersWithPropMutableLiveData.postValue(emptyList())
            } else {
                usersWithPropMutableLiveData.postValue(listOf(uwp))
            }
            isLoadingLiveData.postValue(false)
        }
    }

    fun removeUser(uwp: UsersWithProperties) {
        viewModelScope.launch {
            userRepository.removeUser(uwp.user.id)
            loadUsersList()
        }
    }

    fun deleteAttachment(attach: Attachments) {
        coroutineScope.launch {
            userRepository.deleteAttachment(attach)
            //loadMessagesList(attach.message)
        }
    }

    fun initDB() {
        viewModelScope.launch {
            userRepository.initDB()
            loadUsersList()
        }
    }

    fun deleteMessage(msg: Messages) {
        coroutineScope.launch {
            userRepository.deleteMessage(msg)
        }
    }


    fun createMessage(sId: Long, rId: Long) {
        coroutineScope.launch {
            userRepository.addMessage(sId, rId)
            //loadMessagesList(sId)
        }
    }

    suspend fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    override fun onCleared() {
        super.onCleared()
        //coroutineScope.coroutineContext.cancel()
    }

    fun saveUserWithPropirties(
        uwp: UsersWithProperties
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
