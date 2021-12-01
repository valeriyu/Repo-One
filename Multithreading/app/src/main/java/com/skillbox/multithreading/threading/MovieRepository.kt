package com.skillbox.multithreading.threading

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.skillbox.multithreading.networking.Movie
import com.skillbox.multithreading.networking.Network
import com.skillbox.multithreading.networking.Network.MOVIE_API_KEY
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MovieRepository {

    private val movieIds = listOf(
        "tt0133093", //Матрица
        "tt0137523", //Бойцовский клуб
        "tt0109830", //Форрест Гамп
        "tt0068646",  //Крёстный отец
        "tt0167261"  //Властелин колец: Две крепости
    )

    fun getMovieById(movieId: String): Movie? {
        return Network.api().getMovieById(movieId, MOVIE_API_KEY).execute()
            .body() //ignore exceptions
    }

    fun gethMoviesEx(
        onMoviesFetched: (moviesList: List<Movie>, fetchTime: Long) -> Unit
    ) {
        val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
        val KEEP_ALIVE_TIME = 1L
        val KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS

        val threadPoolExecutor = ThreadPoolExecutor(
            NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT, LinkedBlockingQueue()
        )

        Log.d("ThreadTest", "gethMoviesEx start on ${Thread.currentThread().name}")

        val startTime = System.currentTimeMillis()
        val allMovies = Collections.synchronizedList(mutableListOf<Movie>())

        /*movieIds.map{ movieId ->
            threadPoolExecutor.execute {
                val movie = getMovieById(movieId)
                allMovies += movie
            }*/

            movieIds.map{ movieId ->
                threadPoolExecutor.submit {
                    val movie = getMovieById(movieId)
                    allMovies += movie
                }
        }
        threadPoolExecutor.shutdown()
        threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);

        val requestTime = System.currentTimeMillis() - startTime
        onMoviesFetched(allMovies, requestTime)
    }


    fun gethMovies(
        onMoviesFetched: (moviesList: List<Movie>, fetchTime: Long) -> Unit
    ) {
        Log.d("ThreadTest", "gethMovies start on ${Thread.currentThread().name}")
        Thread {

            val startTime = System.currentTimeMillis()
            val allMovies = Collections.synchronizedList(mutableListOf<Movie>())
            val threads = movieIds.map { movieId ->
                Thread {
                    val movie = getMovieById(movieId)
                    allMovies += movie
                }
            }
            threads.forEach { it.start() }
            threads.forEach { it.join() }

            val requestTime = System.currentTimeMillis() - startTime

            onMoviesFetched(allMovies, requestTime)
        }.start()
        Log.d("ThreadTest", "gethMovies end on ${Thread.currentThread().name}")
    }
}