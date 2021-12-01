package com.valeriyu.notifications.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.valeriyu.notifications.Message
import com.valeriyu.notifications.MsgWihUserName
import com.valeriyu.notifications.databinding.ItemMessageBinding


class MessagesListAdapter() : ListAdapter<MsgWihUserName, MessagesListAdapter.Holder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
                ItemMessageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilCallback() : DiffUtil.ItemCallback<MsgWihUserName>() {
        override fun areItemsTheSame(oldItem: MsgWihUserName, newItem: MsgWihUserName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MsgWihUserName, newItem: MsgWihUserName): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(
            val binding: ItemMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: MsgWihUserName) {
            binding.messageTextView.text = msg.toString()
                .replace("MsgWihUserName", "")
                .replace("(", "")
                .replace(")", "")
                .replace(",", "\n")
        }
    }
}