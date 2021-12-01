package com.skillbox.github.ui.repository_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.github.R
import kotlinx.android.synthetic.main.fragment_repository_list.*

class RepositoryListFragment : Fragment(R.layout.fragment_repository_list) {

    private var listAdapter: ReposittoryListAdapter? = null

    //private val viewModel: RepositoriesViewModel by activityViewModels()
    private val viewModel: RepositoriesViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initList()
        bindViewModel()
    }


    private fun initList() {
        listAdapter = ReposittoryListAdapter({ pos ->
            var repo = listAdapter!!.currentList.get(pos).name
            var owner = listAdapter!!.currentList.get(pos)!!.owner.username
            val action =
                RepositoryListFragmentDirections.actionRepositoryListFragmentToRepoDetalInfoFragment(
                    owner, repo
                )
            findNavController().navigate(action)
        }
        )

        with(repositoriesList) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
        viewModel.getPubRepositories()
    }

    private fun bindViewModel() {
        viewModel.reposList.observe(viewLifecycleOwner) { newList ->
            listAdapter?.submitList(newList)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            enableControls(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun enableControls(enable: Boolean) {
        if (enable) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}