package com.valeriyu.scopedstorage

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.valeriyu.scopedstorage.databinding.DialogAddMovieBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddMovieFragment : BottomSheetDialogFragment() {
    private lateinit var createDocumentLauncher: ActivityResultLauncher<String>
    private var curкUri: Uri? = null

    private val viewModel: AddMovieViewModel by viewModels()

    private var _binding: DialogAddMovieBinding? = null

    private val binding: DialogAddMovieBinding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCreateDocumentLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAddMovieBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()

        //binding.urlTextField.editText?.setText("https://ochepyatki.ru/o_download.php?id=59520&f=31627900600")
        binding.urlTextField.editText?.setText("https://filesamples.com/samples/video/mp4/sample_960x400_ocean_with_audio.mp4")
        binding.nameTextField.editText?.setText("test.mp4")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViewModel() {
        binding.saveAsButton.setOnClickListener {
            val name = binding.nameTextField.editText?.text?.toString().orEmpty()
            createDocumentLauncher.launch(name)
        }

        binding.saveButton.setOnClickListener {
            val url = binding.urlTextField.editText?.text?.toString().orEmpty()
            val name = binding.nameTextField.editText?.text?.toString().orEmpty()
            viewModel.saveMovie(name, url)
        }

        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::setLoading)
        viewModel.toastLiveData.observe(viewLifecycleOwner) {
            toast(it)
        }
        viewModel.saveSuccessLiveData.observe(viewLifecycleOwner) {
            dismiss()
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.contentGroup.isVisible = isLoading.not()
    }

    private fun initCreateDocumentLauncher() {
        createDocumentLauncher = registerForActivityResult(
            ActivityResultContracts.CreateDocument()
        ) { uri ->
            handleCreateFile(uri)
        }
    }

    private fun handleCreateFile(uri: Uri?) {
        var url = binding.urlTextField.editText?.text?.toString().orEmpty()
        if (uri == null || url.isEmpty()) {
            toast("file not created")
            return
        }
        viewModel.saveFile(url, uri)
    }
}