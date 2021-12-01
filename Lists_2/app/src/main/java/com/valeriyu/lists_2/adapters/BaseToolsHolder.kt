package com.valeriyu.lists_2.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.valeriyu.lists_2.R
import kotlinx.android.synthetic.main.item_tool.view.*


abstract class BaseToolsHolder(
    binding: ViewBinding,
    onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    var view = binding.root as ViewGroup

    init {
        view.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    protected fun bindMainInfo(
        name: String,
        pictLink: String,
        description: String
    ) {
        view.nameTextView.text = name
        view.descriptionTextView.text = description

        Glide.with(itemView)
            .load(pictLink)
            .error(R.drawable.ic_baseline_cloud_off_24)
            .placeholder(R.drawable.ic_baseline_cloud_download_24)
            .into(view.pictImageView)
    }

}

/*abstract class BaseToolsHolder(
    //override val containerView: View,
    val view: View,
    onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(view), LayoutContainer {


    private val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
    private val pictImageView: ImageView = view.findViewById(R.id.pictImageView)


    //private val _pictImageView: TextView = containerView.pictImageView

    init {
        view.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    protected fun bindMainInfo(
        name: String,
        pictLink: String,
        description: String
    ) {



        nameTextView.text = name
        descriptionTextView.text = description

        Glide.with(itemView)
            .load(pictLink)
            .error(R.drawable.ic_baseline_cloud_off_24)
            .placeholder(R.drawable.ic_baseline_cloud_download_24)
            .into(pictImageView)
    }
}*/
