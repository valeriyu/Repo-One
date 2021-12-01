package com.valeriyu.roomdao

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.roomdao.data.db.models.users.UsersWithProperties
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.*

class UserAdapterDelegate(
    private val onUserClick: (UsersWithProperties) -> Unit,
    private val onDeleteUser: (UsersWithProperties, Int) -> Unit
) : AbsListItemAdapterDelegate<UsersWithProperties, UsersWithProperties, UserAdapterDelegate.Holder>() {

    override fun isForViewType(
        item: UsersWithProperties,
        items: MutableList<UsersWithProperties>,
        position: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return parent.inflate(R.layout.item_user).let {
            Holder(it, onUserClick, onDeleteUser)
        }
    }

    override fun onBindViewHolder(
        item: UsersWithProperties,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View,
        private val onUserClick: (UsersWithProperties) -> Unit,
        private val onDeleteUser: (UsersWithProperties, Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var currentUser: UsersWithProperties? = null

        init {
            containerView.setOnClickListener { currentUser?.let(onUserClick) }
            removeUserButton.setOnClickListener {
                currentUser?.let { u -> onDeleteUser(u, R.id.removeUserButton) }
            }

            editUserButton.setOnClickListener {
                currentUser?.let { u -> onDeleteUser(u, R.id.editUserButton) }
            }
        }

        fun bind(usersWithProp: UsersWithProperties) {
            currentUser = usersWithProp
            phoneTextView.text =
                "${usersWithProp.user.phone} \n${usersWithProp.properties?.name}"
            Glide.with(itemView)
                .load(usersWithProp.properties?.avatar)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_emoji)
                .into(avatarImageVIew)
        }
    }
}
