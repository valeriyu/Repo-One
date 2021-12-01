package com.valeriyu.roomdao

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.roomdao.data.db.models.users.User
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.*

class UserAdapterDelegate(
    private val onUserClick: (User) -> Unit,
    private val onDeleteUser: (User, Int) -> Unit
) : AbsListItemAdapterDelegate<User, User, UserAdapterDelegate.Holder>() {

    override fun isForViewType(
        item: User,
        items: MutableList<User>,
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
        item: User,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View,
        private val onUserClick: (User) -> Unit,
        private val onDeleteUser: (User, Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var currentUser: User? = null

        init {
            containerView.setOnClickListener { currentUser?.let(onUserClick) }
            removeUserButton.setOnClickListener {
                currentUser?.let { u -> onDeleteUser(u, R.id.removeUserButton) }
            }

            editUserButton.setOnClickListener {
                currentUser?.let { u -> onDeleteUser(u, R.id.editUserButton) }
            }
        }

        fun bind(usersWithProp: User) {
            currentUser = usersWithProp
            phoneTextView.text =
                "${usersWithProp.phone} \n${usersWithProp.name}"
            Glide.with(itemView)
                .load(usersWithProp.avatar)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_emoji)
                .into(avatarImageVIew)
        }
    }
}
