package com.valeriyu.moshi

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

import com.valeriyu.moshi.*

class Repository {

    fun getMovie(title: String,
                   callback: (respString:String) -> Unit,
                   errCallback: (err: String) -> Unit

    ): Call {
        return HTTP.getMovie(title).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: java.io.IOException) {
                    Log.e("Server", "execute request error = ${e.message}", e)
                    e.message?.let { errCallback(it) }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseString = response.body?.string().orEmpty()
                        // val movies = parseResponseString(responseString)
                        callback(responseString)
                    } else {
                        callback("")
                    }
                }
            })
        }
    }

  /*  private fun parseResponseString(responseString: String): List<com.valeriyu.moshi.Movie> {
        return try {
            val jsonObject = JSONObject(responseString)
            val movieArray = jsonObject.getJSONArray("Search")

            (0 until movieArray.length()).map { index -> movieArray.getJSONObject(index) }
                    .map { movieJsonObject ->
                        Movie(
                                title = movieJsonObject.getString("Title"),
                                year = movieJsonObject.getString("Year"),
                                id = movieJsonObject.getString("imdbID"),
                                type = movieJsonObject.getString("Type"),
                                poster = movieJsonObject.getString("Poster")
                        )
                        //Movie(id = id, title = title, year = year)
                    }

        } catch (e: JSONException) {
            Log.e("Server", "parse response error = ${e.message}", e)
            emptyList()
        }
    }*/


}