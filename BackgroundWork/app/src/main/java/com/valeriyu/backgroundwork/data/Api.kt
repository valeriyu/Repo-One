package com.valeriyu.notifications

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {
    @GET
    suspend fun _getFile(
        @Url url: String
    ): ResponseBody

    @GET
    suspend fun getFile(
        @Url url: String
    ): Response<ResponseBody>
}
