package com.skillbox.multithreading.threading

import android.annotation.SuppressLint
import android.os.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.multithreading.MoviesAdapter
import com.skillbox.multithreading.R
import com.skillbox.multithreading.networking.Movie
import com.skillbox.multithreading.networking.Network
import com.skillbox.multithreading.toast
import kotlinx.android.synthetic.main.fragment_threading.*


const val LOG_TAG = "_threading_"

class ThreadingFragment : Fragment(R.layout.fragment_threading) {

    private val viewModel: ThreadingViewModel by viewModels()
    private var moviesAdapter: MoviesAdapter? = null
    private lateinit var mHandler: Handler

    val mainHandler: Handler
        get() = mHandler

    private fun initHandlerCallBack() {
        mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                var newList = msg.obj as List<Movie>
                moviesAdapter?.submitList(newList)
                statusTextView.text = ""
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addFab.setOnClickListener {
            swipeRefreshLayout.isRefreshing = true
            //viewModel.requestMoviesEx(mHandler)
            viewModel.requestMoviesWithContext(this)
            return@setOnClickListener
        }

        initList()
        observeViewModelState()
        initHandlerCallBack()
        showExeption()

        swipeRefreshLayout.setOnRefreshListener {
            setOnRefreshListener()
        }
    }

    private fun setOnRefreshListener() {
        viewModel.requestMovies()

        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }


    private fun showExeption() {
        // Network.getMovieById("tt0073486")

        try {
            Network.getMovieById("tt0073486")
        } catch (e: Exception) {
            //statusTextView.text = e.toString()
            statusTextView.text = e.stackTraceToString()
        }
    }

    private fun observeViewModelState() {
        viewModel.movies.observe(viewLifecycleOwner) { newList ->

            // moviesAdapter?.submitList(newList)

            mHandler.post { moviesAdapter?.submitList(newList) }
            mHandler.postDelayed({
                activity.toast("Список обновлён")
            }, 1000)

            statusTextView.isVisible = newList.isEmpty()
            swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun initList() {

        moviesAdapter = MoviesAdapter()

        with(moviesList) {
            adapter = moviesAdapter
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
        // toolsList.scrollToPosition(tools.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        moviesAdapter = null
    }

}