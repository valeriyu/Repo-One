package com.valeriyu.testappforcustomcontentprovider

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.valeriyu.testappforcustomcontentprovider.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = Repository(application)

    private val toastLiveEvent = SingleLiveEvent<String>()
    val toastEvent: SingleLiveEvent<String>
        get() = toastLiveEvent
    private val isLoadingLiveData = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData
    private val firstStartLiveData = MutableLiveData<Boolean>(false)
    val firstStart: LiveData<Boolean>
        get() = firstStartLiveData


    fun downloadFile(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoadingLiveData.postValue(true)
                repository.downloadFile(url)
                toastLiveEvent.postValue("Файл ${url.substringAfterLast('/')} - успешно загружен")
            } catch (t: Throwable) {
                toastLiveEvent.postValue(t.message)
            } finally {
                isLoadingLiveData.postValue(false)
            }
        }
    }
}
