package com.skillbox.github

import android.annotation.SuppressLint
import android.util.Log
import com.skillbox.github.ui.current_user.CurrentUser
import com.skillbox.github.ui.current_user.UpdReqBody
import com.skillbox.github.ui.repository_list.Repository
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@SuppressLint("LongLogTag")

class AppRepository {

    suspend fun setStar(
        owner: String, repo: String, star: Boolean
    ): Int =
        if (star) {
            HTTP.githubApi.starRepository(owner, repo).code()
        } else {
            HTTP.githubApi.unstarRepository(owner, repo).code()
        }

    suspend fun getCurrentUserInfo() = HTTP.githubApi.getCurrentUserInfo()

    suspend fun peopleWhoUserFollows() = HTTP.githubApi.peopleWhoUserFollowsSuspend()

    suspend fun getPubRepositories() =
        withContext(Dispatchers.IO) {
            var starredListRepos: List<Repository>
            var listRepos: List<Repository> = emptyList()
            //Здесь специально два синхронных рапроса
            starredListRepos = HTTP.githubApi.getStarredRepositories().execute().body().orEmpty()
            listRepos = HTTP.githubApi.getPubRepositories().execute().body().orEmpty()

            var filter = starredListRepos.orEmpty().map {
                it.id
            }
            listRepos.filter {
                it.id in filter
            }.map {
                it.isStarred = true
            }
            return@withContext listRepos
        }

    suspend fun checkRepositoryIsStarred(
        owner: String, repo: String
    ): Boolean {
        return suspendCancellableCoroutine<Boolean> { continuation ->
            var call = HTTP.githubApi.checkRepositoryIsStarred(owner, repo)

            continuation.invokeOnCancellation {
                call.cancel()
            }

            call.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.code() == 204) {
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    suspend fun updateCurrentUserInfo(body: UpdReqBody): Int =
        HTTP.githubApi.updateCurrentUserInfo(body).code()

    suspend fun getRepo(owner: String, repo: String): Repository? {
        var res = HTTP.githubApi.getRepo(owner, repo)
        Log.d("_getRepo_", "err = ${res.raw()}")
        return res.body()
    }
}





