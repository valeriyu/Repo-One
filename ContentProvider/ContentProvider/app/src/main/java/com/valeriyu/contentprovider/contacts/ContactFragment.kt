package com.valeriyu.contentprovider.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.valeriyu.contentprovider.R
import com.valeriyu.contentprovider.databinding.FragmentContactListBinding
import com.valeriyu.contentprovider.utils.hideKeyboardAndClearFocus
import com.valeriyu.contentprovider.utils.toast
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.fragment_contact_add.saveButton
import kotlinx.android.synthetic.main.view_toolbar.*
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest


@SuppressLint("LongLogTag")
class ContactFragment : Fragment(R.layout.fragment_contact) {

    private val viewModel by viewModels<ViewModel>()
    private val args: ContactFragmentArgs by navArgs()
    private var phonesListAdapter: ArrayAdapter<String>? = null
    private var mailListAdapter: ArrayAdapter<String>? = null
    private var binding: FragmentContactListBinding? = null


    fun initLists() {
        phonesListAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        pthonesListView.setAdapter(phonesListAdapter)
        mailListAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        emailAdrListView.setAdapter(mailListAdapter)

        pthonesListView.setOnItemClickListener { _, view, _, _ ->
            viewModel.deletePhone(args.contactId, (view as TextView).text.toString())
        }

        emailAdrListView.setOnItemClickListener { _, view, _, _ ->
            viewModel.deleteEmail(args.contactId, (view as TextView).text.toString())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        bindViewModel()
        initLists()
        binding = FragmentContactListBinding.inflate(layoutInflater)
        //viewModel.getFhonesList(args.contactId)
        viewModel.getContBody(args.contactId)

    }

    private fun initToolbar() {
        toolbar.inflateMenu(R.menu.contact_menu)
        toolbar.setTitle("${args.contactName}")
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_remove -> {
                    //deleteContact(args.contactId)
                    viewModel.deleteContact(args.contactId)
                    findNavController().popBackStack()
                    //viewModel.loadList()
                    true
                }
                else -> {
                    findNavController().popBackStack()
                    viewModel.loadList()
                    false
                }
            }
        }
    }

    private fun bindViewModel() {
        saveButton.setOnClickListener {
            saveBodyWithPermissionCheck()
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            requireActivity().hideKeyboardAndClearFocus()
            phoneTextField.editText?.setText("")
            emailTextField?.editText?.setText("")
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.contBody.observe(viewLifecycleOwner) {
            phonesListAdapter?.clear()
            phonesListAdapter?.addAll(it.phones)
            pthonesListView.setSelection(it.phones.size - 1)

            phoneTextField.editText?.setText("")
            //phonesListAdapter?.notifyDataSetChanged()

            mailListAdapter?.clear()
            mailListAdapter?.addAll(it.email)
            emailAdrListView.setSelection(it.email.size - 1)
            emailTextField?.editText?.setText("")
            //mailListAdapter?.notifyDataSetChanged()
            requireActivity().hideKeyboardAndClearFocus()
        }
    }

    private fun deleteContact(contactId: Long) {
        viewModel.deleteContact(contactId)
    }

    private fun saveBodyWithPermissionCheck() {
        constructPermissionsRequest(
            Manifest.permission.WRITE_CONTACTS,
            onShowRationale = ::onContactPermissionShowRationale,
            onPermissionDenied = ::onContactPermissionDenied,
            onNeverAskAgain = ::onContactPermissionNeverAskAgain
        ) {
            var phone = phoneTextField?.editText?.text?.toString()
            var email = emailTextField?.editText?.text?.toString()

            if (phone != null) {
                if (email != null) {
                    viewModel.saveBody(args.contactId, phone, email)
                }
            }
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
        binding = null
        if (isRemoving) {
            requireActivity().hideKeyboardAndClearFocus()
        }
    }
}