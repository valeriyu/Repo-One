package com.valeriyu.networking.networking

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

const val MOVIE_API_KEY = "75e0bd7a"

//http://www.omdbapi.com/?apikey=[yourkey]&s=

object HTTP {
    val flipperNetworkPlugin = NetworkFlipperPlugin()

    var okHttpClient = OkHttpClient.Builder()

            .addNetworkInterceptor(
                    CustomHeaderInterceptor(MOVIE_API_KEY)
            )

            .addNetworkInterceptor(
                    HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
            )

            .addNetworkInterceptor(FlipperOkhttpInterceptor(flipperNetworkPlugin))
            .build()


    fun findMovies(searchStr: String = "", type: String = "", year: String = ""): Call {

        var httpUrlBuilder = HttpUrl.Builder()

        httpUrlBuilder
                .scheme("http")
                .host("www.omdbapi.com")
                //.addQueryParameter("apikey", MOVIE_API_KEY)

        if (searchStr != "") {
            httpUrlBuilder.addQueryParameter("s", searchStr)
        }
        if (type != "") {
            httpUrlBuilder.addQueryParameter("type", type)
        }

            httpUrlBuilder.addQueryParameter("page", "1")

        if (year != "") {
            httpUrlBuilder.addQueryParameter("y", year)
        }


        return okHttpClient.newCall(Request.Builder()
                .get()
                .url(httpUrlBuilder.build())
                .build())
    }
}