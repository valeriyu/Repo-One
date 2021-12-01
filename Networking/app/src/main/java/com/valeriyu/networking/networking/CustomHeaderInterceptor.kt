package com.valeriyu.networking.networking

import okhttp3.Interceptor
import okhttp3.Response

class CustomHeaderInterceptor(
        private val apiey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val origUrl = chain.request().url
        val newUrl = origUrl.newBuilder().addQueryParameter("apikey", apiey).build()

        val modifiedRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

        val response = chain.proceed(modifiedRequest)
        return response
    }
}