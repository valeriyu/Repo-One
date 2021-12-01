package com.valeriyu.networking

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import bolts.Bolts
import kotlinx.android.synthetic.main.movies_list_fragment.*
import okhttp3.internal.notify
import java.util.concurrent.CountDownLatch


class MoviesFragment : Fragment(R.layout.movies_list_fragment) //, SearchView.OnQueryTextListener
{
    private var moviesListAdapter: MoviesListAdapter? = null

    //private val viewModel: MoviesViewModel by viewModels()
    private val viewModel: MoviesViewModel by activityViewModels()

    private var searchSting = ""
    private var type = ""
    private var page = 1
    private var year = ""

    private fun lockUI() {
        linearLayout.isEnabled = false
        moviesList.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun unLockUI() {
        linearLayout.isEnabled = true
        moviesList.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    }

    private fun clrList() {
        moviesListAdapter?.submitList(emptyList())
    }

    private fun observeViewModelState() {

        viewModel.movies.observe(viewLifecycleOwner) { newList ->
            clrList()
            moviesListAdapter?.submitList(newList)
        }

        viewModel.showToast.observe(viewLifecycleOwner) {
            activity.toast("Ничего не найдено !!!")
        }

        viewModel.error.observe(viewLifecycleOwner) { err ->
            //viewModel.movies.removeObservers(viewLifecycleOwner)
            var _err = err
            if (err == null || err == "") {
                _err = " Неизвестная ошибка !!!"
            }
                val action =  MoviesFragmentDirections.actionMoviesFragmentToErrotFragment(_err!!)
                    findNavController().navigate(action)

                Thread {
                    viewModel.clrList()
                }.start()
                //findNavController().navigate(R.id.action_moviesFragment_to_errotFragment)

        }
        viewModel.isDone.observe(viewLifecycleOwner) {
            if (it!!) {
                unLockUI()
            } else {
                lockUI()
            }
        }
    }


    private fun initList() {

        moviesListAdapter = MoviesListAdapter()

        with(moviesList) {
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

    fun initSearch() {

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
                    //lockUI()
                    year = yearEditText.text.toString()
                    viewModel.findMovies(searchSting, type, year)
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

        val items = resources.getStringArray(R.array.genreNames)
//val adapter = ArrayAdapter(requireContext(), R.layout.menu_item, items)
        val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
        (menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (menu.editText as? AutoCompleteTextView)?.threshold = 0
        (menu.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
//movie, series, episode
            type = when (position) {
                1 -> "series"
                2 -> "movie"
                3 -> "episode"
                else -> ""
            }
/*         clrList()
lockUI()
viewModel.findMovies(searchSting, type)*/
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initList()
        initSearch()
        observeViewModelState()
        unLockUI()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movies_list_fragment, container, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        moviesListAdapter = null
    }

}