package com.valeriyu.flow

import com.valeriyu.flow.models.Search
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MovieApi {
    @GET("/")
    suspend fun findMovies(
        @Query("s") searchStr: String,
        @Query("type") type: String
    ): Search

    @GET
    suspend fun getFile(
        @Url url: String
    ): Response<ResponseBody>
}