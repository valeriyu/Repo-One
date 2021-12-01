package com.skillbox.github

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object HTTP {

    private val okhttpClient = OkHttpClient.Builder()
     .addNetworkInterceptor(
            object:  Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()

                    val modifiedRequest = originalRequest.newBuilder()
                        .addHeader("Authorization" , "token ${HTTP.Token.value}")
                        .build()

                    val response = chain.proceed(modifiedRequest)
                    return response
                }
            }
        )

       //.addNetworkInterceptor(
       //     CustomHeaderInterceptor("Authorization" , "token ${HTTP.Token.value}"))

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

    object Token{
        var value: String = ""
    }

}

