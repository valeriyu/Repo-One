package com.skillbox.github

import android.annotation.SuppressLint
import com.skillbox.github.utils.SingleLiveEvent
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.IOException


@SuppressLint("LongLogTag")

object HTTP {
    private val okhttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val modifiedRequest = originalRequest.newBuilder()
                        .addHeader("Authorization", "token ${HTTP.Token.value}")
                        .build()
                    val response = chain.proceed(modifiedRequest)
                    return response
                }
            }
        )
        .addInterceptor(
            object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val response = chain.proceed(request)
          /*          if (request.method !="GET" && request.url.toString().contains("https://api.github.com/user/starred")) {
                        if (response.code == 204) {
                        } else if (response.code == 404) {
                        }
                    }*/
                    return response
                }
            }
        )
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        //.addNetworkInterceptor(FlipperOkhttpInterceptor(flipperNetworkPlugin))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okhttpClient)
        .build()

    val githubApi: GithubApi
        get() = retrofit.create()

    object Token {
        var value: String = ""
    }
}

