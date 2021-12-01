package com.valeriyu.flow

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Url

object Network {

    const val MOVIE_API_KEY = "75e0bd7a"

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(
            object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val origUrl = chain.request().url
                    val newUrl = origUrl.newBuilder().addQueryParameter("apikey", MOVIE_API_KEY).build()

                    val modifiedRequest = originalRequest.newBuilder()
                        .url(newUrl)
                        .build()

                    val response = chain.proceed(modifiedRequest)
                    return response
                }
            }
        )
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("http://www.omdbapi.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    fun api(): MovieApi {
        return retrofit.create()
    }

   /* private val retrofit = Retrofit.Builder()
        .baseUrl("https://google.com")
        .client(okhttpClient)
        .build()

    val api: Api
        get() = retrofit.create()*/

}