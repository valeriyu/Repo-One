package com.valeriyu.roomdao

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.valeriyu.roomdao.data.db.models.users.UsersWithProperties
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.view_toolbar.*

class UsersFragment : Fragment(R.layout.fragment_users) {

    private val viewModel by viewModels<MessangerViewModel>()
    private var userAdapter: UserListAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initList()
        addUserButton.setOnClickListener {
            findNavController().navigate(UsersFragmentDirections.actionUsersFragmentToAddUserFragment(0))
        }
        bindViewModel()
        viewModel.initDB()
        viewModel.loadUsersList()
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.user_list_title)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            //findNavController().popBackStack()
            activity?.finish()
        }
    }

    private fun initList() {
        userAdapter = UserListAdapter(
            ::navigateToMsgList,
            { u, btn ->
                when (btn) {
                    R.id.removeUserButton ->{
                        viewModel.removeUser(u)
                    }
                    R.id.editUserButton -> {
                        findNavController().navigate(UsersFragmentDirections.actionUsersFragmentToAddUserFragment(u.user.id))
                        //viewModel.editUser(u)
                    }
                }
            }

            //viewModel::removeUser
        )
        with(userList) {
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }

    /*  private fun navigateToUserDetails(user: UsersWithProperties) {
          val direction = UsersFragmentDirections.actionUsersFragmentToAddUserFragment(user.user.id)
          findNavController().navigate(direction)
      }*/

    private fun navigateToMsgList(user: UsersWithProperties) {
        val direction =
            UsersFragmentDirections.actionUsersFragmentToMessagesListFragment(user.user.id)
        findNavController().navigate(direction)
    }

    private fun bindViewModel() {
        viewModel.usersWithProp.observe(viewLifecycleOwner) {
            userAdapter!!.items = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userAdapter = null
    }
}