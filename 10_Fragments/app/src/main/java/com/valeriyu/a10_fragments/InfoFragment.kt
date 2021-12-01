package com.valeriyu.a10_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_info.*

private const val ARG_INFO = "info"


class InfoFragment : Fragment(R.layout.fragment_info) {

    private var info: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            info = it.getString(ARG_INFO)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewInfo.text = info
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(info: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_INFO, info)
                }
            }
    }
}