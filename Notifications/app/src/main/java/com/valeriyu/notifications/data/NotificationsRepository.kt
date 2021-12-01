package com.valeriyu.notifications

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.core.app.NotificationManagerCompat
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.withTransaction
import com.valeriyu.notifications.app.Database
import com.valeriyu.notifications.models.Promotions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.File

class NotificationsRepository(
    private val context: Context
) {
    private val dao = Database.instance.dao()
    private var observer: ContentObserver? = null

    var messagesListFlow = emptyList<List<MsgWihUserName>>().asFlow()
        get() = dao.observeMessages()


    suspend fun insertMessage(msg: Message){
        withContext(Dispatchers.IO) {
            Database.instance.withTransaction {
                dao.inserUser(msg.user)
                dao.insertMessage(msg)
            }
        }
    }

    suspend fun insertMessages(list: List<Message>){
        withContext(Dispatchers.IO) {
            dao.insertMessages(list)
        }
    }

    suspend fun insertPromotions(list: List<Promotions>){
         withContext(Dispatchers.IO) {
             dao.insertPromotions(list)
         }
    }


    suspend fun downloadFileWithProgress(
        name: String,
        url: String
     ) {
        withContext(Dispatchers.IO) {
            val downloadFolder = context.getExternalFilesDir("download_files")
            val file = File(downloadFolder, name)
            val resp = Networking.api.getFile(url)
            val body = resp.body()
            var type = (body as ResponseBody).contentType()?.type

            try {
                file.outputStream().buffered().use { fileOutputStream ->
                    body
                        ?.byteStream()
                        .use { inputStream ->
                            inputStream?.copyTo(fileOutputStream)
                        }
                }
            } catch (t: Throwable) {
                file.delete()
                error(t)
            }
        }
    }
}
