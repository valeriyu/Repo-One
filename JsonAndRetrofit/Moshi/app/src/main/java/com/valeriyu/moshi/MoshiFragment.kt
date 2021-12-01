package com.valeriyu.moshi


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.fragment_moshi.*


class MoshiFragment : Fragment(R.layout.fragment_moshi) {

    private val viewModel: MoshiViewModel by viewModels()
    private var movie:Movie? = Movie()
    private var jsonString: String = ""
    private var searchSting = "Terminator 2"

    private fun hideKeyboard(view: View) {
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.respLveData.observe(viewLifecycleOwner) { js ->
            if (js != null) {
                jsonString = js
            }

            jsonTextView.text = jsonString


            viewModel.movieText.observe(viewLifecycleOwner){
                movieTextView.text = it
            }

            searchView.setQuery(searchSting, false)
            searchView.setIconifiedByDefault(false)
            searchView.setSubmitButtonEnabled(true)
            searchView.setQueryHint("Искать фильм")
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                @SuppressLint("ResourceType")
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query == null) {
                        searchSting = ""
                    } else searchSting = query

                    if (searchSting.trim() != "") {
                        hideKeyboard(searchView)
                        viewModel.getMovie(searchSting)
                    } else {
                        activity.toast("Введите строку поиска")
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
//                clrList()
//                viewModel.findMovies(searchSting, type)
                    return true
                }
            })
        }

        btnMovie.setOnClickListener {
            movie = convertMovieJsonToInstance(jsonString)
            // movieTextView.text = movie.toString()
            viewModel.movieText.value = movie.toString()

        }
        btnScores.setOnClickListener {
            movie?.scores?.put("Источник оценки -  ${(1..100).random() }",  "${(1..100).random()} %")
            //movieTextView.text = convertInstanceMovieToJson()
            var buf = convertInstanceMovieToJson()
            Log.d("NEW_ SCORE", buf)
            viewModel.movieText.value = buf
        }

        viewModel.getMovie(searchSting)
    }


    private fun convertMovieJsonToInstance(jsonString: String): Movie? {
        val moshi = Moshi.Builder()
            .add(CustomAdapter())
            .build()
        val adapter = moshi.adapter(Movie::class.java).nonNull()

        try {
            return adapter.fromJson(jsonString)

        } catch (e: Exception) {
            jsonTextView.text = "parse error = ${e.message}"
            return Movie()
        }

    }

    private fun convertInstanceMovieToJson(): String? {
        val moshi = Moshi.Builder()
            //.add(CustomAdapter())
            .build()

        val adapter = moshi.adapter(Movie::class.java).nonNull()

        try {
            //return adapter.toJson(movie)
            return adapter.toJson(movie)

        } catch (e: Exception) {
            return "parse error = ${e.message}"
        }
    }
}