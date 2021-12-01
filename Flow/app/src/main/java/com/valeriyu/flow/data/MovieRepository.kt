package com.valeriyu.flow.data

import android.content.Context
import android.net.Uri
import com.valeriyu.flow.Database
import com.valeriyu.flow.Network
import com.valeriyu.flow.models.Movies
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.File

@InternalCoroutinesApi
class MovieRepository(private var context: Context) {
    private val moviesDao = Database.instance.moviesDao()

    private val mutableMoviesList = MutableStateFlow(emptyList<Movies>())
    val moviesList: StateFlow<List<Movies>>
        get() = mutableMoviesList

    fun changeState(list: List<Movies>) {
        mutableMoviesList.value = list
    }

    private var _oserveMovies = moviesDao.observeMovies()
    var observeMovies = emptyList<List<Movies>>().asFlow()
        get() = _oserveMovies

    suspend fun searchMovies(text: String, type: String = "") {
        var newList: List<Movies>
        if (text.isBlank()) return

        try {
            newList = Network.api().findMovies(text, type).search.orEmpty().toMutableSet().toList()
            changeState(newList)
            CoroutineScope(Dispatchers.IO).launch {
                insertMovies(newList)
            }
        } catch (t: Throwable) {
            val typesList = if (type == "") listOf("movie", "series", "episode")
            else listOf(type)

            if (text.length < 3 || text.isBlank()) {
                newList = emptyList()
            } else {
                newList = moviesDao.findMovies("%${text}%", typesList).orEmpty()
            }
            changeState(newList)
        }
    }

    private suspend fun insertMovies(list: List<Movies>) {
        //error("Что то не так с базой")
        val listFromDb = moviesDao.getMoviesList().orEmpty()
        val diffList = list.filterNot { it.imdbID in listFromDb.map { it.imdbID } }

        Timber.e("difList - > ${diffList.map { it.title }}")
        if (diffList.isEmpty()) return

        val cacheDir = context.cacheDir
        for (i in 0..diffList.size - 1) {
            var movie = diffList[i]
            var child = Uri.parse(movie.poster).lastPathSegment.toString()
            val file = File(cacheDir, child)

            if(file.exists() || movie.poster.trim() == "N/A" || movie.poster.trim() == "") continue

            val resp = Network.api().getFile(movie.poster)
            val body = resp.body()
            try {
                file.outputStream().buffered().use { fileOutputStream ->
                    body
                        ?.byteStream()
                        .use { inputStream ->
                            inputStream?.copyTo(fileOutputStream)
                        }
                }
                movie.poster_cache_path = file.path
                moviesDao.updateMovies(movie)
            } catch (t: Throwable) {
                file.delete()
                movie.poster_cache_path = ""
                //error(t)
            }
        }
        //moviesDao.updateMovies(fList)
        moviesDao.insertMovies(diffList)
    }
}