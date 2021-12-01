package com.valeriyu.contentprovider

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: Fragment(R.layout.fragment_main) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contactsButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToContactListFragment())
        }

        fileSahreButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToFragmentFiles())
        }
    }

}