package com.valeriyu.testappforcustomcontentprovider


import android.content.Context
import android.os.Environment
import androidx.lifecycle.*
import com.valeriyu.files.Networking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


class Repository(cont: Context) {
    private var context = cont

    //private val progressLiveEvent = SingleLiveEvent<Int>()
    private val progressLiveEvent = SingleLiveEventK<Int>(0)
    val progressEvent: SingleLiveEventK<Int>
        get() = progressLiveEvent

      suspend fun downloadFile(url: String): Int {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        //var fn = System.currentTimeMillis().toString() + "_" + url.substringAfterLast('/')
        var fn = url.substringAfterLast('/')

        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            error("Внешнее хранилище не доступно")
        }
/*
        if (sharedPrefs.getString(url, null) != null) {
            error("Файл уже был скачан")
        }*/

        val downloadFolder = context.getExternalFilesDir("download_files")
        val file = File(downloadFolder, fn)

        val rc = Networking.api
            .getFile(url)
        val body = rc.body()
        val code = rc.code()

        if(code !=200){
            return code
        }

        try {
            file.outputStream().buffered().use { fileOutputStream ->
                body
                    ?.byteStream()
                    .use { inputStream ->
                        inputStream?.copyTo(fileOutputStream)
                    }

 /*               sharedPrefs.edit()
                    .putString(url, fn)
                    .commit()*/
            }
        } catch (t: Throwable) {
            sharedPrefs.edit().remove(url).commit()
            file.delete()
            error(t)
        }
        return 200
    }




    companion object {
        private const val SHARED_PREFS_NAME = "download_files_shared_prefs"
        private const val FIRST_START_SHARED_PREFS = "first_start_info_shared_prefs"
        private const val FIRST_START_FLAG = "first_start_flag"
    }
}