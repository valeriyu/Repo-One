package com.valeriyu.viewmodelandnavigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_tools_list.*

private const val ARG_PARAM1 = "selector"
const val MAX_ITEMS_COUNT = 50

@SuppressLint("LongLogTag")

class ToolsListFragment() :
    Fragment(R.layout.fragment_tools_list) {

    private val toolsListViewModel: ToolsViewModel by viewModels()
    private var toolsAdapter: ToolsAdapter? = null
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private fun observeViewModelState() {
        toolsListViewModel.tools.observe(viewLifecycleOwner) { newTools ->
            toolsAdapter?.items = newTools
            statusTextView.isVisible = newTools.isEmpty()
            updStatus()
        }

        toolsListViewModel.showToast.observe(viewLifecycleOwner) {
            //activity.toast("Элемент добавлен")
            activity.toast("Элемент удален.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tools_list, container, false)
    }

    private fun initScrollListener() {
        // return

        scrollListener = object :
            EndlessRecyclerViewScrollListener(toolsList.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore(page, totalItemsCount, view)
            }

            override fun chLastVisable(lastVisibleItemPosition: Int, totalItemsCount: Int) {
                textViewCounts?.text =
                    "  Всего элементов: ${totalItemsCount}"
                textViewLastVis?.text = "Последний видимый элем. : ${lastVisibleItemPosition}"
            }
        }
        toolsList.addOnScrollListener(scrollListener)
    }

    private fun enableScrollListener() {

        // return

        scrollListener.resetState()

        Handler().postDelayed({
            toolsList.addOnScrollListener(scrollListener)
        }, 100)
    }

    private fun disableScrollListener() {

        // return

        toolsList.removeOnScrollListener(scrollListener)
    }


    private fun loadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
        toolsListViewModel.loadMore(totalItemsCount)
        // updateToolsList()
        updStatus()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initList()

        addFab.setOnClickListener {
            addFabOnClick()
        }


        Handler()
            .postDelayed(Runnable {
                initScrollListener()
            }, 1000)

        observeViewModelState()
        updStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toolsAdapter = null
    }

    private fun initList() {
        toolsAdapter = ToolsAdapter({ url ->
            val action =
                ToolsListFragmentDirections.actionToolsListFragmentToDetailsFragment(url)
            findNavController().navigate(action)
            //deleteItem(position)
        }, { pos ->
            deleteItem(pos)
        })
        with(toolsList) {
            adapter = toolsAdapter
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
        toolsList.scrollToPosition(1)
        updStatus()
    }

    private fun deleteItem(position: Int) {

        disableScrollListener()

        toolsListViewModel.deleteItem(position)
        // updateToolsList()

        enableScrollListener()
        updStatus()
    }

    fun updStatus() {

        //return

        val handler = Handler()
        handler.postDelayed(Runnable {
            textViewCounts?.text =
                "  Всего элементов: ${toolsList.adapter?.getItemCount()}"
            textViewLastVis?.text =
                "Последний видимый элем. : ${(toolsList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()}"
        }, 100)
    }

    fun addFabOnClick() {

        if (toolsAdapter!!.items.size >= MAX_ITEMS_COUNT) {
            return
        }

        disableScrollListener()
        toolsListViewModel.addItem()
        // updateToolsList()
        toolsList.scrollToPosition(4)

        enableScrollListener()
        updStatus()
    }

    companion object {
        @JvmStatic
        fun newInstance(sel: Int = 0) =
            ToolsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, sel)
                }
            }
    }
}

