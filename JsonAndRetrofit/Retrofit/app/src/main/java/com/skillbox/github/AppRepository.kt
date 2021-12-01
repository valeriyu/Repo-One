package com.skillbox.github

import com.skillbox.github.ui.current_user.CurrentUser
import com.skillbox.github.ui.current_user.UpdReqBody
import com.skillbox.github.ui.repository_list.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository {

    fun updateCurrentUserInfo(
        body: UpdReqBody,
        onComplete: (Any?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        HTTP.githubApi.updateCurrentUserInfo(body)!!
            .enqueue(CallBackObject(onComplete, onError) as Callback<CurrentUser?>)
    }

    fun getPubRepositories(
        onComplete: (Any?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        var call = HTTP.githubApi.getPubRepositories()
        call.enqueue(object : Callback<List<Repository>> {
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                Thread {

                    var starredListRepos =
                        HTTP.githubApi.getStarredRepositories().execute().body().orEmpty()
                    var list = response.body().orEmpty()
                    var filter = starredListRepos.orEmpty().map {
                        it.id
                    }
                    list.filter {
                        it.id in filter
                    }.map {
                        it.isStarred = true
                    }
                    onComplete(list)

                }.start()
            }
            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                onError(t)
            }
        }
        )
    }


    fun _getPubRepositories(
        onComplete: (Any?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        HTTP.githubApi.getPubRepositories()
            .enqueue(CallBackObject(onComplete, onError) as Callback<List<Repository>>)
    }

    fun checkRepositoryIsStarred(
        owner: String, repo: String,
        onComplete: (Any?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        HTTP.githubApi.checkRepositoryIsStarred(owner, repo)
            ?.enqueue(CallBackObject(onComplete, onError) as Callback<Any>)
    }

    fun getRepo(
        owner: String, repo: String,
        onComplete: (Any?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        HTTP.githubApi.getRepo(owner, repo)
            ?.enqueue(CallBackObject(onComplete, onError) as Callback<Repository?>)
    }

    fun setStar(
        owner: String, repo: String, star: Boolean,
        onComplete: (Any?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (star) {
            HTTP.githubApi.starRepository(owner, repo)
                .enqueue(CallBackObject(onComplete, onError) as Callback<Any>)
        } else {
            HTTP.githubApi.unstarRepository(owner, repo)
                .enqueue(CallBackObject(onComplete, onError) as Callback<Any>)
        }
    }

    fun getCurrentUserInfo(
        onComplete: (Any?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        HTTP.githubApi.getCurrentUserInfo()!!
            .enqueue(CallBackObject(onComplete, onError) as Callback<CurrentUser>)
    }


    private class CallBackObject(
        private val onComplete: (Any?) -> Unit,
        private val onError: (Throwable) -> Unit
    ) : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.body() == null) {
                onComplete(response.code())
                return
            }

            if (response.isSuccessful) {
                onComplete(response.body())
            } else {
                onError(RuntimeException("incorrect status code"))
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            onError(t)
        }
    }
}





