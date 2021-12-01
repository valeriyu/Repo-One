package com.valeriyu.backgroundwork.data

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.valeriyu.backgroundwork.ui.WorkManagerFragment
import com.valeriyu.backgroundwork.worker.DownloadWorker
import com.valeriyu.notifications.Networking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

class BgwRepository(
    private val context: Context
) {

    suspend fun downloadFile(url: String): ListenableWorker.Result {
        var res = withContext(Dispatchers.IO) {
            val downloadFolder = context.getExternalFilesDir(null);
            val file = File(downloadFolder, Uri.parse(url).lastPathSegment)
            try {
                val resp = Networking.api.getFile(url)
                val body = resp.body()
                val type = (body as ResponseBody).contentType()?.type
                Timber.d(type)
                file.outputStream().buffered().use { fileOutputStream ->
                    body
                        .byteStream()
                        .use { inputStream ->
                            inputStream.copyTo(fileOutputStream)
                        }
                }
                Timber.d("Конец загрузки")
                ListenableWorker.Result.success()
            } catch (t: Throwable) {
                file.delete()
                Timber.d("Ошибка загрузки: ${t.message}")
                if (t is CancellationException){
                    ListenableWorker.Result.success()
                }else{
                    ListenableWorker.Result.retry()
                }
            }
        }
        return res
    }
}