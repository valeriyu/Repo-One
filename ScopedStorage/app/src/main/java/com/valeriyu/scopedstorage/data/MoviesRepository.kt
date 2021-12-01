package com.valeriyu.scopedstorage

import android.annotation.SuppressLint
import android.content.*
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.core.content.MimeTypeFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File

class MoviesRepository(
    private val context: Context
) {

    private var observer: ContentObserver? = null

    fun observeImages(onChange: () -> Unit) {
        observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                onChange()
            }
        }
        context.contentResolver.registerContentObserver(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            true,
            observer!!
        )
    }

    fun unregisterObserver() {
        observer?.let { context.contentResolver.unregisterContentObserver(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getMovies(): List<Movie> {
        val movList = mutableListOf<Movie>()

        withContext(Dispatchers.IO) {
            val bundle = Bundle()
            bundle.putInt("android:query-arg-match-trashed", 1)

            var _cursor = if (haveQ()) {
                context.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null,
                    bundle,
                    null
                )
            } else {
                context.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )}

                _cursor?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val videoPath =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                        //val size = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))

                        val path = if (haveQ()) {
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RELATIVE_PATH))
                                .orEmpty() + "\n$videoPath"
                        } else {
                            videoPath
                        }

                        val size = File(videoPath).length()
                        var extension = MimeTypeMap.getFileExtensionFromUrl(videoPath)
                        var mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                        if (mType == null || mType.contains("video").not() || size == 0L) {
                            // continue
                        }

                        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                        val name =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                        val uri =
                            ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                id
                            )

                        var movie = Movie(
                            id = id,
                            uri = uri,
                            name = name,
                            size = size,
                            path = path
                        )

                        if (haveR()) {
                            movie.favorite =
                                cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.IS_FAVORITE)) == "1"
                            movie.trashed =
                                cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.IS_TRASHED)) == "1"
                        }
                        movList += movie
                    }
                }
            }
            return movList
        }

        suspend fun saveMovie(name: String, url: String) {
            withContext(Dispatchers.IO) {
                val volume = if (haveQ()) {
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                } else {
                    MediaStore.VOLUME_EXTERNAL
                }

                //val volume = MediaStore.VOLUME_EXTERNAL

                val movieUri = MediaStore.Video.Media.getContentUri(volume)
                val movieDetails = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, name)
                    //put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Video.Media.MIME_TYPE, "video/*")
                    //put(MediaStore.Video.Media.MIME_TYPE, "*/*")
                    if (haveQ()) {
                        put(MediaStore.Video.Media.IS_PENDING, 1)
                    }
                }

                var uri = context.contentResolver.insert(movieUri, movieDetails)!!
                downloadFile(url, uri)
                makeImageVisible(uri)

                if (Build.VERSION.SDK_INT < 29) {
                    val imageDetails = ContentValues().apply {
                        put(MediaStore.Video.Media.DISPLAY_NAME, name)
                    }
                    context.contentResolver.update(uri, imageDetails, null, null)
                }
            }
        }

        private fun makeImageVisible(movieUri: Uri) {
            if (haveQ().not()) return

            val imageDetails = ContentValues().apply {
                put(MediaStore.Video.Media.IS_PENDING, 0)
            }
            context.contentResolver.update(movieUri, imageDetails, null, null)
        }

        suspend fun downloadFile(url: String, uri: Uri) {
            withContext(Dispatchers.IO) {
                val rc = Networking.api.getFile(url)
                val body = rc.body()
                var type = (body as ResponseBody).contentType()?.type
                if (type != "video") {
                    error("Это не видео")
                }

                val code = rc.code()
                if (code != 200) {
                    error("Ошибка загрузки файла")
                }

                context.contentResolver.openOutputStream(uri)?.buffered().use { outputStream ->
                    body.byteStream()
                        .buffered()
                        .use { inputStream ->
                            inputStream.copyTo(outputStream!!)
                        }
                }
            }
        }

        suspend fun deleteMovie(id: Long) {
            val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            withContext(Dispatchers.IO) {
                context.contentResolver.delete(uri, null, null)
            }
        }

        fun addToFavorites(media: List<Movie>, state: Boolean): IntentSender {
            if (haveR().not()) error("Это работает только на Android - 11")
            val uris = media.map { it.uri }
            return MediaStore.createFavoriteRequest(
                context.contentResolver,
                uris,
                state
            ).intentSender
        }

        fun addToTrash(media: List<Movie>, state: Boolean): IntentSender {
            if (haveR().not()) error("Это работает только на Android - 11")
            val uris = media.map { it.uri }
            return MediaStore.createTrashRequest(context.contentResolver, uris, state).intentSender
        }
    }