package com.valeriyu.moshi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.Call

class MoshiViewModel : ViewModel() {
    private var call: Call? = null
    private val repository = Repository()

    var movieText = MutableLiveData<String>()

    private val errLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errLiveData

    private val showToastLiveData = SingleLiveEventK<Unit>()
    val showToast: SingleLiveEventK<Unit>
        get() = showToastLiveData

    private val _respLveData = SingleLiveEventK<String>()
    val respLveData: SingleLiveEventK<String>
        get() = _respLveData

    fun getMovie(title: String) {
        call = repository.getMovie(title, { resp ->
            respLveData.postValue(resp)
            call = null

        }, { err ->
            errLiveData.postValue(err)
            call = null
        }
        )
    }

    override fun onCleared() {
        super.onCleared()
        call = null
    }
}