package com.valeriyu.flow.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.valeriyu.flow.databinding.FragmentMoviesBinding
import com.valeriyu.flow.models.MovieType
import com.valeriyu.flow.utils.onCheckedChangeFlow
import com.valeriyu.flow.utils.textChangedFlow
import com.valeriyu.notifications.ViewBindingFragment
import com.valeriyu.notifications.toast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MoviesFragment() :
    ViewBindingFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {
    private var moviesListAdapter: MoviesListAdapter? = null
    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            var queryFlow = binding.searchEditText.textChangedFlow().onStart { emit("") }
            var movieTypeFlow = binding.radioGroup.onCheckedChangeFlow().onStart { emit(MovieType.ALL) }
            viewModel.bind(queryFlow, movieTypeFlow)
        }

        viewModel.moviesListFlow
            .onEach {
                if (it.isEmpty().not()) toast("В базе ${it.size} фильмов")
            }.launchIn(lifecycleScope)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it!!) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            toast(it)
        }

        viewModel.movies.observe(viewLifecycleOwner) { newList ->
            moviesListAdapter?.submitList(newList.orEmpty())
        }

    }

    private fun initList() {
        moviesListAdapter = MoviesListAdapter()

        with(binding.moviesList) {
            adapter = moviesListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            // itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        moviesListAdapter = null
    }
}