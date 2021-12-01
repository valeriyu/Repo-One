package com.valeriyu.contentprovider

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_files.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class FragmentFiles : Fragment(R.layout.fragment_files) {
    private val viewModel by viewModels<FilesViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindViewModel()


        downloadButton.setOnClickListener {
            val url = searchInput.text.toString()
            downloadFile(url)
        }

        shareButton.setOnClickListener {
            val url = searchInput.text.toString()
            shareFile(url)
        }

        searchInput.setText("https://cdn.pixabay.com/photo/2019/10/02/07/50/rhino-4520317_1280.jpg")
    }

    private fun bindViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            enableControls(it)
        }
        viewModel.toastEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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

    private fun shareFile(url: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@launch
            val dir = requireContext().getExternalFilesDir("download_files")


            var fn = url.substringAfterLast('/')
            val file = File(dir, fn)

            if (file.exists().not()) return@launch

            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${BuildConfig.APPLICATION_ID}.file_provider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, uri)
                type = requireContext().contentResolver.getType(uri)
                setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        }
    }
}