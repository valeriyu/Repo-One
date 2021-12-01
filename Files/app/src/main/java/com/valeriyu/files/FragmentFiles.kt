package com.valeriyu.files

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_files.*


//@RequiresApi(Build.VERSION_CODES.O)

class FragmentFiles : Fragment(R.layout.fragment_files) {
    private val viewModel by viewModels<FilesViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindViewModel()
        viewModel.getStartFiles()

        downloadButton.setOnClickListener {
            val url = searchInput.text.toString()
            downloadFile(url)
        }

        searchInput.setText("https://github.com/square/retrofit/blob/master/README.md")
    }

    private fun bindViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            enableControls(it)
        }
        viewModel.toastEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.firstStart.observe(viewLifecycleOwner) {
            if (it) {
                statusTextView.visibility = View.VISIBLE
                horizProgressBar.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            } else {
                statusTextView.visibility = View.GONE
                horizProgressBar.visibility = View.GONE
            }
        }

        viewModel.repoProgressEvent.observe(viewLifecycleOwner) {
            if (it != null) {
                horizProgressBar.progress = it
            }
        }
    }

    private fun enableControls(enable: Boolean) {
        if (enable) {
            progressBar.visibility = View.VISIBLE
            searchInput.isEnabled = false
            downloadButton.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            searchInput.isEnabled = true
            downloadButton.isEnabled = true
        }
    }

    private fun downloadFile(url: String) {
        viewModel.downloadFile(url)
    }
}