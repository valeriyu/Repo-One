package com.skillbox.github

import com.skillbox.github.ui.current_user.CurrentUser
import com.skillbox.github.ui.current_user.UpdReqBody
import com.skillbox.github.ui.repository_list.Repository
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface GithubApi {

    @DELETE("/user/starred/{owner}/{repo}")
    suspend fun unstarRepository(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Response<Void>

    @Headers("Content-Length: 0")
    @PUT("/user/starred/{owner}/{repo}")
    suspend fun starRepository(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Response<Void>

    @GET("user")
    suspend fun getCurrentUserInfo(
    ): CurrentUser

    @GET("/user/following")
    suspend fun peopleWhoUserFollowsSuspend():List<CurrentUser>

    @PATCH("user")
    suspend fun updateCurrentUserInfo(@Body body: UpdReqBody): Response<Void>

    @GET("repos/{owner}/{repo}")
    suspend fun  getRepo(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Response<Repository>



    @GET("/user/starred/{owner}/{repo}")
    fun checkRepositoryIsStarred(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Call<Any>

    @GET("repositories")
    fun getPubRepositories(): Call<List<Repository>>

    @GET("/user/starred")
    fun getStarredRepositories(): Call<List<Repository>>

  /*  @GET("/user/starred/{owner}/{repo}")
    fun checkRepositoryIsStarred(
        @HeaderMap headers: Map<String, String>,
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Call<Repository?>?*/

}



