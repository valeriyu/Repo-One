package com.valeriyu.networking

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.skillbox.multithreading.networking.Movie
import okhttp3.Call

class MoviesViewModel : ViewModel() {
    private var call: Call? = null
    private val userRepository = MovieRepository()

    private val moviesLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = moviesLiveData

    private val errLiveData = SingleLiveEventK<String>()
    //private val errLiveData = MutableLiveData<String>()
    val error: SingleLiveEventK<String>
        get() = errLiveData

   private val _qOkEvent = SingleLiveEventK<Unit>()
    val qOkEvent:SingleLiveEventK<Unit>
        get() = _qOkEvent

    private val mIsDone = SingleLiveEventK<Boolean>()
    val isDone: SingleLiveEventK<Boolean>
        get() = mIsDone


    private val showToastLiveData = SingleLiveEventK<Unit>()
    val showToast: SingleLiveEventK<Unit>
        get() = showToastLiveData


    private var rText = ""
    private var rType = ""
    private var rYear = ""


    fun findMovies(text: String, type: String = "", year: String = "") {
        rText = text
        rType = type
        rYear = year

        mIsDone.postValue(false)
        call = userRepository.findMovies(text, type, year, { movies ->
            _qOkEvent.postValue(Unit)
            moviesLiveData.postValue(movies)
            call = null
            if(movies.isEmpty()) {
                showToastLiveData.postValue(Unit)
            }
            mIsDone.postValue(true)
        }, { err ->
            errLiveData.postValue(err)
            mIsDone.postValue(true)
        }
        )

    }

    fun clrList(){
        moviesLiveData.postValue(emptyList())
    }

    fun repFindMovies() {
        findMovies(rText, rType, rYear)
    }

    /* fun findMovies(text: String, type: String = "", year: String = "") {
         if (text == "" ) {
             moviesLiveData.postValue(emptyList())
         }
         call = userRepository.findMovies(text, type, year) { movies, err ->
             if (err != null) {
                 errLiveData.postValue(err)
             } else {
                 moviesLiveData.postValue(movies)
             }
             call = null
         }
     }*/


    override fun onCleared() {
        super.onCleared()
        call = null
    }
}