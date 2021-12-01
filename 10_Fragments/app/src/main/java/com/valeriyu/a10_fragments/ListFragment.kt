package com.valeriyu.a10_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment

class ListFragment : Fragment(R.layout.fragment_list) {

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //view.let { it as ViewGroup }
        (view as ViewGroup)
            .children
            .mapNotNull { it as? TextView }
            .forEach { textView ->
                textView.setOnClickListener(View.OnClickListener {
                    var infoFragment = InfoFragment.newInstance(textView.text.toString())
                    (parentFragment as? FragmentOpenListner)?.OpenFragment(infoFragment, "INFO")
                })
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }
}