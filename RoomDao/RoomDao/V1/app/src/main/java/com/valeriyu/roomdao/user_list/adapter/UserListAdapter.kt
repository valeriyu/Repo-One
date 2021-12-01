package com.valeriyu.roomdao

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.valeriyu.roomdao.data.db.models.users.UsersWithProperties
import kotlin.reflect.KFunction1


class UserListAdapter(
    private val onUserClick: (UsersWithProperties) -> Unit,
    private val onDeleteUser: (UsersWithProperties, Int) -> Unit
) : AsyncListDifferDelegationAdapter<UsersWithProperties>(UserDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(UserAdapterDelegate(onUserClick, onDeleteUser))
    }

    class UserDiffUtilCallback : DiffUtil.ItemCallback<UsersWithProperties>() {
        override fun areItemsTheSame(
            oldItem: UsersWithProperties,
            newItem: UsersWithProperties
        ): Boolean {
            return oldItem.user.id == newItem.user.id
        }

        override fun areContentsTheSame(
            oldItem: UsersWithProperties,
            newItem: UsersWithProperties
        ): Boolean {
            return oldItem == newItem
        }
    }

}
