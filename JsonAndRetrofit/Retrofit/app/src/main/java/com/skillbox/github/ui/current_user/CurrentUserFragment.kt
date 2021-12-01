package com.skillbox.github.ui.current_user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.skillbox.github.R
import com.skillbox.github.ui.repository_list.RepositoriesViewModel
import com.skillbox.github.utils.toast
import kotlinx.android.synthetic.main.fragment_current_user.*
import kotlinx.android.synthetic.main.fragment_current_user.progressBar
import kotlinx.android.synthetic.main.fragment_current_user.view.*
import kotlinx.android.synthetic.main.fragment_repository_list.*

class CurrentUserFragment: Fragment(R.layout.fragment_current_user) {
    private val viewModel: RepositoriesViewModel by viewModels()
    private lateinit var updReqBody:UpdReqBody

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnUpdate.setOnClickListener {
            updReqBody = UpdReqBody(
            name = nameEditText.text.toString(),
            company = companyEditText.text.toString(),
            location = locationEditText.text.toString()
            )
            viewModel.updateCurrentUserInfo(updReqBody)
        }

        viewModel.getCurrentUserInfo()

        viewModel.currUser.observe(viewLifecycleOwner){
            if(it == null) return@observe

            userInfoTextView.text = " id: ${it.id}\n Логин: ${it.username}\n"
            nameEditText.setText(it.name.orEmpty())
            companyEditText.setText(it.company.orEmpty())
            locationEditText.setText(it.location.orEmpty())
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            enableControls(it)
        }
    }
    private fun enableControls(enable: Boolean) {
        if (enable) {
            currUserFragmentConteiner.progressBar.visibility = View.VISIBLE
        } else {
            currUserFragmentConteiner.progressBar.visibility = View.GONE
        }
    }
}