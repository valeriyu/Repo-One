package com.skillbox.github

import com.skillbox.github.ui.current_user.CurrentUser
import com.skillbox.github.ui.current_user.UpdReqBody
import com.skillbox.github.ui.repository_list.Repository
import retrofit2.Call
import retrofit2.http.*


interface GithubApi {

    @PATCH("user")
    fun updateCurrentUserInfo(@Body body: UpdReqBody): Call<CurrentUser?>?

    @GET("/user/starred")
    fun getStarredRepositories(): Call<List<Repository>>

    @DELETE("/user/starred/{owner}/{repo}")
    fun unstarRepository(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Call<Any>

    @PUT("/user/starred/{owner}/{repo}")
    fun starRepository(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Call<Any>

    @GET("/user/starred/{owner}/{repo}")
    fun checkRepositoryIsStarred(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Call<Any>?

    @GET("repos/{owner}/{repo}")
    fun getRepo(
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Call<Repository?>?

     @GET("user")
     fun getUser(@HeaderMap headers: Map<String, String>): Call<CurrentUser?>?

    @GET("user")
    fun getCurrentUserInfo(
    ): Call<CurrentUser>

    @GET("repositories")
    fun getPubRepositories(): Call<List<Repository>>


  /*  @GET("/user/starred/{owner}/{repo}")
    fun checkRepositoryIsStarred(
        @HeaderMap headers: Map<String, String>,
        @Path("owner") owner: String?,
        @Path("repo") repo: String?
    ): Call<Repository?>?*/

}



