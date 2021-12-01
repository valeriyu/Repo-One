package com.valeriyu.notifications

import com.valeriyu.notifications.data.DownloadState
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.*
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber
import java.io.IOException


//class Networking(onProgress: (Int) -> Unit) {
object Networking {
    private val progressListener = object : ProgressListener {
        var firstUpdate = true
        override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
            if (done) {
                Timber.d("completed")
            } else {
                if (firstUpdate) {
                    firstUpdate = false
                    if (contentLength == -1L) {
                        Timber.d("content-length: unknown")
                    } else {
                        Timber.d("content-length: ${contentLength}")
                    }
                }
                //Timber.d("Bytes read => ${bytesRead}")
                if (contentLength != -1L) {
                    var progress = 100 * bytesRead / contentLength
                    DownloadState.changeDownloadState(progress)
                    //Timber.d("done => ${progress}")
                    //onProgress(progress.toInt())
                }
            }
        }
    }

    private val okhttpClient = OkHttpClient.Builder()
        /*  .addNetworkInterceptor(
              HttpLoggingInterceptor()
                  .setLevel(HttpLoggingInterceptor.Level.BODY)
          )*/
        .addNetworkInterceptor(Interceptor { chain: Interceptor.Chain ->
            val originalResponse = chain.proceed(chain.request())
            originalResponse.newBuilder()
                .body(ProgressResponseBody(originalResponse.body, progressListener))
                .build()
        })
        .followRedirects(true)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://google.com")
        .client(okhttpClient)
        .build()

    val api: Api
        get() = retrofit.create()

    private class ProgressResponseBody(
        private val responseBody: ResponseBody?,
        private val progressListener: ProgressListener
    ) : ResponseBody() {
        private var bufferedSource: BufferedSource? = null
        override fun contentType(): MediaType? {
            return responseBody!!.contentType()
        }

        override fun contentLength(): Long {
            return responseBody!!.contentLength()
        }

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = source(responseBody!!.source()).buffer()
            }
            return bufferedSource!!
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L

                @Throws(IOException::class)
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    progressListener.update(
                        totalBytesRead,
                        responseBody!!.contentLength(),
                        bytesRead == -1L
                    )
                    return bytesRead
                }
            }
        }
    }

    private interface ProgressListener {
        fun update(bytesRead: Long, contentLength: Long, done: Boolean)
    }
}



