package com.valeriyu.roomdao

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.valeriyu.roomdao.data.db.models.users.User


class UserListAdapter(
    private val onUserClick: (User) -> Unit,
    private val onDeleteUser: (User, Int) -> Unit
) : AsyncListDifferDelegationAdapter<User>(UserDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(UserAdapterDelegate(onUserClick, onDeleteUser))
    }

    class UserDiffUtilCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem == newItem
        }
    }

}
