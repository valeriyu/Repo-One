package com.skillbox.github.ui.current_user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.skillbox.github.R
import com.skillbox.github.databinding.ItemRepositoryBinding
import com.skillbox.github.databinding.ItemUserBinding
import com.skillbox.github.ui.current_user.CurrentUser
import kotlinx.android.synthetic.main.item_user.*


class UserListAdapter(
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<CurrentUser, UserListAdapter.Holder>(
    DiffUtilCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(

            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilCallback() : DiffUtil.ItemCallback<CurrentUser>() {
        override fun areItemsTheSame(oldItem: CurrentUser, newItem: CurrentUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CurrentUser, newItem: CurrentUser): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(
        val binding: ItemUserBinding,
        private val onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var view = binding.root as ViewGroup

        init {
            view.setOnClickListener {
                onItemClick(adapterPosition)
            }

            /*  view.setOnLongClickListener {
                  onItemLongClick(adapterPosition)
                  return@setOnLongClickListener true*/
        }

        fun bind(user: CurrentUser) {
            binding.userTextView.text =
                "id: ${user.id}\n Логин: ${user.username}\n Компания: ${user.company}\n"
            Glide.with(itemView)
                .load(user.avatar_url)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_emoji)
                .into(binding.avatarImageView)
        }
    }
}
