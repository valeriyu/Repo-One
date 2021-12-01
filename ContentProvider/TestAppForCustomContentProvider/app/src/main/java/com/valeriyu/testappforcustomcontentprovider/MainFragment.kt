package com.valeriyu.testappforcustomcontentprovider

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.view_toolbar.*

class MainFragment: Fragment(R.layout.fragment_main) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fileSahreButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToFragmentFiles())
        }

        customContentproviderButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToCoursesListFragment())
        }

        initToolbar()
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
    }
}