package com.valeriyu.contentprovider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.valeriyu.contentprovider.contacts.Contact
import com.valeriyu.contentprovider.databinding.ItemContactBinding

class ContactsListAdapter(
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<Contact, ContactsListAdapter.Holder>(ContactsListAdapter.DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsListAdapter.Holder {
        return Holder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )
    }

    override fun onBindViewHolder(holder: ContactsListAdapter.Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilCallback() : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(
        val binding: ItemContactBinding,
        private val onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(cont: Contact) {
            binding.contactNameTextView.setText("${cont.name}" + " " + "${cont.famile_name}")
        }
    }
}
