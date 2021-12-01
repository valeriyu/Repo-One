package com.skillbox.github.ui.current_user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.skillbox.github.R
import com.skillbox.github.ui.repository_list.RepositoriesViewModel
import com.skillbox.github.ui.repository_list.ReposittoryListAdapter
import com.skillbox.github.utils.toast
import kotlinx.android.synthetic.main.fragment_current_user.*
import kotlinx.android.synthetic.main.fragment_current_user.progressBar
import kotlinx.android.synthetic.main.fragment_current_user.view.*
import kotlinx.android.synthetic.main.fragment_repository_list.*

class CurrentUserFragment: Fragment(R.layout.fragment_current_user) {
    private val viewModel: RepositoriesViewModel by viewModels()
    private lateinit var updReqBody:UpdReqBody

    private var listAdapter:UserListAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initList()

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

            userTextView.text = " id: ${it.id}\n Логин: ${it.username}\n"
            nameEditText.setText(it.name.orEmpty())
            companyEditText.setText(it.company.orEmpty())
            locationEditText.setText(it.location.orEmpty())

            Glide.with(this)
                .load(it.avatar_url)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_emoji)
                .into(avatarImageView)
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            enableControls(it)
        }

        viewModel.piplFollofs.observe(viewLifecycleOwner){
            listAdapter?.submitList(it)
        }
    }

    private fun enableControls(enable: Boolean) {
        if (enable) {
            currUserFragmentConteiner.progressBar.visibility = View.VISIBLE
        } else {
            currUserFragmentConteiner.progressBar.visibility = View.GONE
        }
    }

    private fun initList(){
        listAdapter = UserListAdapter({})

        with(subscriberList) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            setHasFixedSize(true)
        }
    }
}