package com.valeriyu.notifications.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object DownloadState {

    private val mutableDownloadState = MutableStateFlow(0L)
    val downloadState: StateFlow<Long> = mutableDownloadState

    fun changeDownloadState(progress: Long) {
        mutableDownloadState.value = progress
    }

}