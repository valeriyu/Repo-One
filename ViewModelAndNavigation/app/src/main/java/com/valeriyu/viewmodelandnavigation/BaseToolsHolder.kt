package com.valeriyu.viewmodelandnavigation

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_tool.view.*


abstract class BaseToolsHolder(
    binding: ViewBinding,
    onItemClick: (url: String) -> Unit,
    onItemLongClick: (position :Int) -> Unit

) : RecyclerView.ViewHolder(binding.root) {

    var view = binding.root as ViewGroup

    private var url: String? = "TEST"

    init {
        view.setOnClickListener {
            url?.let{
                onItemClick(it)
            }
           }

        view.setOnLongClickListener {
            onItemLongClick(adapterPosition)
            return@setOnLongClickListener true
        }
    }

    protected fun bindMainInfo(
        name: String,
        pictLink: String,
        description: String
    ) {
        url = pictLink
        view.nameTextView.text = name
        view.descriptionTextView.text = description

        Glide.with(itemView)
            .load(pictLink)
            .error(R.drawable.ic_baseline_cloud_off_24)
            .placeholder(R.drawable.ic_baseline_cloud_download_24)
            .into(view.pictImageView)
    }
}
