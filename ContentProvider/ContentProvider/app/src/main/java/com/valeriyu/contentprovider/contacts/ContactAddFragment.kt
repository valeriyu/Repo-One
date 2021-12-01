package com.valeriyu.contentprovider.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.valeriyu.contentprovider.R
import com.valeriyu.contentprovider.utils.hideKeyboardAndClearFocus
import com.valeriyu.contentprovider.utils.toast
import kotlinx.android.synthetic.main.fragment_contact_add.*
import kotlinx.android.synthetic.main.view_toolbar.*
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest


class ContactAddFragment: Fragment(R.layout.fragment_contact_add) {

    private val viewModel by viewModels<ViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        bindViewModel()
    }

    @SuppressLint("RestrictedApi")
    private fun initToolbar() {
        toolbar.setTitle(R.string.contact_add_title)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel() {
        saveButton.setOnClickListener { saveContactWithPermissionCheck() }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            }
        viewModel.saveSuccessLiveData.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun saveContactWithPermissionCheck() {
        constructPermissionsRequest(
            Manifest.permission.WRITE_CONTACTS,
            onShowRationale = ::onContactPermissionShowRationale,
            onPermissionDenied = ::onContactPermissionDenied,
            onNeverAskAgain = ::onContactPermissionNeverAskAgain) {
            viewModel.save(
                name = nameTextField.editText?.text?.toString().orEmpty(),
                famile_name = famileTextField.editText?.text?.toString().orEmpty(),
                phone = phoneAddTextField.editText?.text?.toString().orEmpty(),
                email = emailAddTextField.editText?.text?.toString().orEmpty()
            )
        }.launch()
    }

    private fun onContactPermissionDenied() {
        toast(R.string.contact_add_permission_denied)
    }

    private fun onContactPermissionShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onContactPermissionNeverAskAgain() {
        toast(R.string.contact_add_permission_never_ask_again)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isRemoving) {
            requireActivity().hideKeyboardAndClearFocus()
        }
    }
}