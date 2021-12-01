package com.valeriyu.files

import android.app.Application
import androidx.lifecycle.*
import com.valeriyu.files.utils.SingleLiveEvent
import com.valeriyu.files.utils.SingleLiveEventK
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

    //var repoProgressEvent = repository.progressEvent
    val repoProgressEvent = Transformations.switchMap(repository.progressEvent) {value -> SingleLiveEventK<Int>(value) }

     fun getStartFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoadingLiveData.postValue(true)
                firstStartLiveData.postValue(true)
                repository.getStartFiles()
            } catch (t: Throwable) {
                toastLiveEvent.postValue(t.message)
            } finally {
                isLoadingLiveData.postValue(false)
                firstStartLiveData.postValue(false)
            }
        }
    }

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
