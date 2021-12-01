package com.valeriyu.backgroundwork.ui

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.valeriyu.backgroundwork.data.BgwRepository
import com.valeriyu.backgroundwork.worker.DownloadWorker
import com.valeriyu.notifications.SingleLiveEventK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class WmViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val permissionsGrantedMutableLiveData = MutableLiveData(true)
    val permissionsGrantedLiveData: LiveData<Boolean>
        get() = permissionsGrantedMutableLiveData

    fun updatePermissionState(isGranted: Boolean) {
        if (isGranted) {
            permissionsGranted()
        } else {
            permissionsDenied()
        }
    }

    fun permissionsGranted() {
        permissionsGrantedMutableLiveData.postValue(true)
    }

    fun permissionsDenied() {
        permissionsGrantedMutableLiveData.postValue(false)
    }
}