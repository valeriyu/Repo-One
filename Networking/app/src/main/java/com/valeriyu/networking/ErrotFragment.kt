package com.valeriyu.networking

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_errot.*


class ErrotFragment : Fragment(R.layout.fragment_errot) {

    private val args: ErrotFragmentArgs by navArgs()

    //private val viewModel: MoviesViewModel by viewModels()
    private val viewModel: MoviesViewModel by activityViewModels()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        errTextView.text = args.errSting

        viewModel.isDone.observe(viewLifecycleOwner) {
            btnNewSearch.isEnabled = it!!
        }


        viewModel.qOkEvent.observe(viewLifecycleOwner) {
            //findNavController().navigate(R.id.action_errotFragment_to_moviesFragment)
            findNavController().popBackStack()
        }

        btnNewSearch.setOnClickListener {
            errTextView.text = ""
            viewModel.repFindMovies()
            // findNavController().navigate(R.id.action_errotFragment_to_moviesFragment)
        }

        viewModel.error.observe(viewLifecycleOwner) { err ->
            errTextView.text = err
        }
    }
}