package com.valeriyu.testappforcustomcontentprovider

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import kotlinx.android.synthetic.main.fragment_course.*
import kotlinx.android.synthetic.main.fragment_custom_content_provider.*
import kotlinx.android.synthetic.main.view_toolbar.*

class CourseFragment : Fragment(R.layout.fragment_course) {
    private val viewModel by activityViewModels<CoursesViewModel>()

    //private val args: CourseFragmentArgs by navArgs()

    //private lateinit var currСourse: Course
    private var currСourse: Course? = null

    private fun initToolbar() {
        toolbar.inflateMenu(R.menu.courses_menu)
        var menu = toolbar.menu
        menu[0].setVisible(false)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_remove -> {
                    currСourse?.let { viewModel.deleteCourse(it.id) }
                    findNavController().popBackStack()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()
        bindViewModel()

        saveCourseButton.setOnClickListener {
            var title = courseTitleTextField.editText?.text ?: return@setOnClickListener
            if (currСourse == null) {
                viewModel.addCourse(title.toString())
            } else {
                viewModel.updateCourse(currСourse!!.id, title.toString())
            }
            findNavController().popBackStack()
        }
    }

    fun bindViewModel() {
        viewModel.currСourse.observe(viewLifecycleOwner) {
            currСourse = it
            if (it != null) {
                courseTitleTextField.editText?.setText(it.title)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            lockUi(it)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun lockUi(cock: Boolean) {
        if(cock) {
            toolbar.isEnabled = false
            courseTitleTextField.isEnabled = false
            saveCourseButton.isEnabled = false
        }else{
            toolbar.isEnabled = true
            courseTitleTextField.isEnabled = true
            saveCourseButton.isEnabled = true
        }
    }

}