package com.valeriyu.contentprovider.contacts

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.valeriyu.contentprovider.ContactsListAdapter
import com.valeriyu.contentprovider.R
import com.valeriyu.contentprovider.utils.toast
import kotlinx.android.synthetic.main.fragment_contact_list.*
import kotlinx.android.synthetic.main.view_toolbar.*
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest


class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private val viewModel by viewModels<ViewModel>()

    //private var contactAdapter: ListAdapter by autoCleared()
    private lateinit var contactAdapter: ContactsListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initList()
        addContactButton.setOnClickListener {
            findNavController().navigate(R.id.action_contactListFragment_to_contactAddFragment)
        }
        bindViewModel()

      /*  Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                Manifest.permission.READ_CONTACTS,
                onShowRationale = ::onContactPermissionShowRationale,
                onPermissionDenied = ::onContactPermissionDenied,
                onNeverAskAgain = ::onContactPermissionNeverAskAgain,
                requiresPermission = { viewModel.loadList() }
            )
                .launch()
        }*/
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                Manifest.permission.READ_CONTACTS,
                onShowRationale = ::onContactPermissionShowRationale,
                onPermissionDenied = ::onContactPermissionDenied,
                onNeverAskAgain = ::onContactPermissionNeverAskAgain,
                requiresPermission = { viewModel.loadList() }
            )
                .launch()
        }
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        toolbar.setTitle(R.string.contact_list_title)
    }

    private fun initList() {
        contactAdapter = ContactsListAdapter { pos ->
            val id = contactAdapter.currentList.get(pos).id
            val name = contactAdapter.currentList.get(pos).name
            val action =
                ContactListFragmentDirections.actionContactListFragmentToContactFragment(id, name)
            findNavController().navigate(action)
        }
        with(contactList) {
            adapter = contactAdapter
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() {
        viewModel.contactsLiveData.observe(viewLifecycleOwner) {
            contactAdapter.submitList(it)
        }
    }


    private fun onContactPermissionDenied() {
        toast(R.string.contact_list_permission_denied)
    }

    private fun onContactPermissionShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onContactPermissionNeverAskAgain() {
        toast(R.string.contact_list_permission_never_ask_again)
    }
}