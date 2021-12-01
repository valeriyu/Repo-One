package com.valeriyu.roomdao

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.valeriyu.roomdao.app.Database
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.ui.MessangerViewModel
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.view_toolbar.*
import kotlinx.coroutines.launch

class UsersFragment : Fragment(R.layout.fragment_users) {

    private val viewModel by viewModels<MessangerViewModel>()
    private var userAdapter: UserListAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initList()
        addUserButton.setOnClickListener {

        /*    val userDao = Database.instance.userDao()
            lifecycleScope.launch {
                var users = List<User>(1) {
                    var rndPhone = List(10) { (0..9).random() }.joinToString("", "+7")
                    User(
                        id = 0,
                        phone = rndPhone,
                        password = "strong password".hashCode(),
                        name = "User # ${rndPhone.substring(8)}",
                        avatar = "https://cdn.pixabay.com/photo/2014/04/03/10/32/user-310807_1280.png"
                    )
                }
                userDao.insertUsers(users)
            }*/

            findNavController().navigate(
                UsersFragmentDirections.actionUsersFragmentToAddUserFragment(
                    0
                )
            )
        }
        bindViewModel()
        viewModel.initDB()
        // viewModel.loadUsersList()
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.user_list_title)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun initList() {
        userAdapter = UserListAdapter(
            ::navigateToMsgList,
            { u, btn ->
                when (btn) {
                    R.id.removeUserButton -> {
                        viewModel.removeUser(u)
                    }
                    R.id.editUserButton -> {
                        findNavController().navigate(
                            UsersFragmentDirections.actionUsersFragmentToAddUserFragment(
                                u.id
                            )
                        )
                    }
                }
            }
        )
        with(userList) {
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }

    private fun navigateToMsgList(user: User) {
        val direction =
            UsersFragmentDirections.actionUsersFragmentToMessagesListFragment(user.id)
        findNavController().navigate(direction)
    }

    private fun bindViewModel() {
  /*      viewModel.usersWithProp.observe(viewLifecycleOwner) {
            userAdapter!!.items = it
        }*/

        viewModel.usersObservable.observe(viewLifecycleOwner) {
            userAdapter!!.items = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userAdapter = null
    }
}