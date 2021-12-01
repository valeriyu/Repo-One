package com.valeriyu.notifications.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.valeriyu.notifications.R
import com.valeriyu.notifications.databinding.FragmentListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class ListFragment : Fragment(R.layout.fragment_list) {

    private val binding: FragmentListBinding by viewBinding(FragmentListBinding::bind)
    private val viewModel: NotificationsViewModel by viewModels()
    private var listAdapter: MessagesListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
    }

    @SuppressLint("SimpleDateFormat")
    private fun bindViewModel() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.moviesListFlow
            .flowOn(Dispatchers.Default)
            .onEach {
                val sdf = SimpleDateFormat("dd-MM-yy HH:mm:ss")
                it.forEach { it.created_at = sdf.format(it.created_at.toLong()) }
                listAdapter?.submitList(it)
            }
            .launchIn(lifecycleScope)

       /* lifecycleScope.launch {
            viewModel.moviesListFlow
                .collect {
                    val sdf = SimpleDateFormat("dd-MM-yy HH:mm:ss")
                    it.forEach { it.created_at = sdf.format(it.created_at.toLong()) }
                    listAdapter?.submitList(it)
                }
        }*/
    }

    private fun initList() {
        listAdapter = MessagesListAdapter()

        with(binding.messagesList) {
            adapter = listAdapter
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
        listAdapter = null
    }
}