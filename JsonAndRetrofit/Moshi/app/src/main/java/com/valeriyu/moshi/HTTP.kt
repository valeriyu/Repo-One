package com.valeriyu.moshi

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

const val MOVIE_API_KEY = "75e0bd7a"

//http://www.omdbapi.com/?apikey=[yourkey]&s=
//http://www.omdbapi.com/?t=title

object HTTP {
    var okHttpClient = OkHttpClient.Builder()

        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()


    fun getMovie(title: String = ""): Call {

        var httpUrlBuilder = HttpUrl.Builder()

        httpUrlBuilder
            .scheme("http")
            .host("www.omdbapi.com")
            .addQueryParameter("apikey", MOVIE_API_KEY)

        httpUrlBuilder.addQueryParameter("t", title)

        return okHttpClient.newCall(
            Request.Builder()
                .get()
                .url(httpUrlBuilder.build())
                .build()
        )
    }
}