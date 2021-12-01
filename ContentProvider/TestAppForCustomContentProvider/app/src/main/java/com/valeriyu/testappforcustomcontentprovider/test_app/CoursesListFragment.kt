package com.valeriyu.testappforcustomcontentprovider

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_custom_content_provider.*
import kotlinx.android.synthetic.main.view_toolbar.*


class CoursesListFragment : Fragment(R.layout.fragment_custom_content_provider) {
    //private val viewModel by viewModels<CoursesViewModel>()
    private val viewModel by activityViewModels<CoursesViewModel>()
    private var coursesListAdapter: CoursesListAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initList()
        bindViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCourses()
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        toolbar.inflateMenu(R.menu.courses_menu)
        var menu = toolbar.menu
        menu[1].setVisible(false)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_add -> {
                    findNavController().navigate(
                        CoursesListFragmentDirections.actionCoursesListFragmentToCourseFragment(0)
                    )
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun lockUi(cock: Boolean) {
        if (cock) {
            linearLayout.isEnabled = false
            toolbar.isEnabled = false
        } else {
            linearLayout.isEnabled = true
            toolbar.isEnabled = true
        }
    }

    fun bindViewModel() {
        viewModel.coursesList.observe(viewLifecycleOwner) {
            coursesListAdapter?.submitList(it)
            coursesListAdapter!!.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            lockUi(it)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initList() {
        coursesListAdapter = CoursesListAdapter { pos ->
            val corseID = coursesListAdapter?.currentList?.get(pos)?.id ?: 0
            if (corseID != 0L) {
                val action =
                    CoursesListFragmentDirections.actionCoursesListFragmentToCourseFragment(corseID)
                viewModel.getCourseById(corseID)
                findNavController().navigate(action)
            }
        }

        with(coursesRecyclerView) {
            adapter = coursesListAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coursesListAdapter = null
    }
}