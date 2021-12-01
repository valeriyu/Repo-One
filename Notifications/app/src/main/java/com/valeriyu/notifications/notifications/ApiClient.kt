package com.valeriyu.notifications.notifications

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object ApiClient {
    private const val BASE_URL = "https://fcm.googleapis.com/"
    private var retrofit: Retrofit? = null
    private val okhttpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(
             HttpLoggingInterceptor()
                 .setLevel(HttpLoggingInterceptor.Level.BODY)
         )
        .followRedirects(true)
        .build()


    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .client(okhttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }
            return retrofit
        }

    val api: ApiInterface?
        get() = client?.create()


    object Token{
        var value: String = ""
    }

    object ServerKey{
        var value: String = "key=AAAA_BqkSvo:APA91bHIEoFRLBPbg4yV7zcJ33RL_t4H2APOpLZvPEqzhCkQEf29GFW2YVlEck3jQsDambKnl2uV00_xJrtP-TRZLuxM7mIEd9QjILRyi50fKDJ-b32KwO9l-Tf1pPwbJxXLPXB98sdl"
    }
}