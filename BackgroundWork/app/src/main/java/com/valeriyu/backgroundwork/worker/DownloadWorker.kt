package com.valeriyu.backgroundwork.worker

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.valeriyu.backgroundwork.data.BgwRepository
import com.valeriyu.notifications.Networking
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.io.File

class DownloadWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val repository = BgwRepository(context)
    override suspend fun doWork(): Result {
        val urlToDownload = inputData.getString(DOWNLOAD_URL_KEY).orEmpty()
        if (urlToDownload.isBlank()) return Result.failure()

        Timber.d("doWork -> work started")
        var result = repository.downloadFile(urlToDownload)
        Timber.d("doWork -> work finish ${result}")
        return result
    }

    companion object {
        const val DOWNLOAD_URL_KEY = "download_url"
        const val DOWNLOAD_WORK_ID = "download_work"
    }
}