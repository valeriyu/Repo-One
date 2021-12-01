package com.skillbox.github.ui.repository_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skillbox.github.R
import com.skillbox.github.databinding.ItemRepositoryBinding
import com.skillbox.github.ui.current_user.CurrentUser


class ReposittoryListAdapter(
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<Repository, ReposittoryListAdapter.Holder>(
    DiffUtilCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(

            ItemRepositoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                ),onItemClick
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilCallback() : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(
            val binding: ItemRepositoryBinding,
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




        fun bind(repo: Repository) {
            binding.infoTextView.text = "id: ${repo.id}\n Название: ${repo.name}\n Владелец: ${repo.owner.username}\n"

            if (repo.isStarred) {
                binding.starImageView.setImageResource(R.drawable.ic_baseline_star_rate_24)
            }else{
                binding.starImageView.setImageResource(R.drawable.ic_baseline_star_outline_24)
            }
        }
    }
}