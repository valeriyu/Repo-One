package com.valeriyu.networking

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.skillbox.multithreading.networking.Movie
import com.skillbox.multithreading.networking.Network
import com.skillbox.multithreading.networking.Network.MOVIE_API_KEY
import com.valeriyu.networking.networking.HTTP
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MovieRepository {

    fun findMovies(text: String, type: String = "", year: String = "",
                   callback: (List<Movie>) -> Unit,
                   errCallback: (err: String) -> Unit

    ): Call {
        return HTTP.findMovies(text, type, year).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: java.io.IOException) {
                    Log.e("Server", "execute request error = ${e.message}", e)
                    e.message?.let { errCallback(it) }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseString = response.body?.string().orEmpty()
                        val movies = parseResponseString(responseString)
                        callback(movies)
                    } else {
                        callback(emptyList())
                    }
                }
            })
        }
    }

    private fun parseResponseString(responseString: String): List<Movie> {
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
    }
}