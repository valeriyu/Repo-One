package com.valeriyu.contentprovider.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.valeriyu.contentprovider.R
import com.valeriyu.contentprovider.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val contactRepository = ContactRepository(application)

    private val errorLiveEvent = SingleLiveEvent<String>()
    val errorLiveData: LiveData<String>
        get() = errorLiveEvent

    private val contactsMutableLiveData = MutableLiveData<List<Contact>>()
    val contactsLiveData: LiveData<List<Contact>>
        get() = contactsMutableLiveData

    private val phListMutableLiveData = MutableLiveData<List<String>>()
    val phList: MutableLiveData<List<String>>
        get() = phListMutableLiveData

    private val contBodyMutableLiveData = MutableLiveData<ContactBody>()
    val contBody: MutableLiveData<ContactBody>
        get() = contBodyMutableLiveData

    private val saveSuccessLiveEvent = SingleLiveEvent<Unit>()
    private val saveErrorLiveEvent = SingleLiveEvent<Int>()

    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessLiveEvent


    fun deletePhone(contactId:Long, phone: String){
        viewModelScope.launch {
            try{
                contactRepository.deletePhone(contactId, phone)
                getContBody(contactId)
            }catch (t:Throwable){
                errorLiveEvent.value = t.message
            }
        }
    }

    fun deleteEmail(contactId:Long, email: String){
        viewModelScope.launch {
            try{
                contactRepository.deleteEmail(contactId, email)
                getContBody(contactId)
            }catch (t:Throwable){
                errorLiveEvent.value = t.message
            }
        }
    }

    fun deleteContact(contactId:Long){
        viewModelScope.launch {
            try{
            contactRepository.deleteContact(contactId)
                loadList()
            }catch (t:Throwable){
                errorLiveEvent.value = t.message
            }
        }
    }

    fun save(name: String, famile_name:String ="",  phone: String, email:String = "") {
        viewModelScope.launch {
            try {
                contactRepository.saveContact(name, famile_name, phone, email )
                saveSuccessLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                showError(t)
            }
        }
    }

    fun saveBody(contactId:Long, phone: String="", email:String = "") {
        viewModelScope.launch {
            try {
                contactRepository.saveBody(contactId, phone, email )
                saveSuccessLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                showError(t)
            }finally {
                getContBody(contactId)
            }
        }
    }

    private fun showError(t: Throwable) {
        errorLiveEvent.postValue(t.message)
        saveErrorLiveEvent.postValue(
            when (t) {
                is IncorrectFormException -> R.string.contact_add_save_error_form
                else -> R.string.contact_add_save_error
            }
        )
    }


    fun loadList() {
        viewModelScope.launch {
            try {
                contactsMutableLiveData.postValue(contactRepository.getAllContacts())
            } catch (t: Throwable) {
                contactsMutableLiveData.postValue(emptyList())
            }
        }
    }

    fun getContBody(contactId: Long) {
        viewModelScope.launch {
            var cb = contactRepository.getContBody(contactId)
            contBodyMutableLiveData.postValue(cb)
        }
    }
}