package com.valeriyu.lists_2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.lists_2.Grid.MyImages
import com.valeriyu.lists_2.R
import com.valeriyu.lists_2.databinding.ItemImageBinding
import com.valeriyu.lists_2.databinding.ItemToolBinding
import com.valeriyu.lists_2.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_image.*
import kotlinx.android.synthetic.main.item_image.view.*

class ImageAdapterDelegate :
    AbsListItemAdapterDelegate<MyImages.Image, MyImages, ImageAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun isForViewType(
        item: MyImages,
        items: MutableList<MyImages>,
        position: Int
    ): Boolean {
        return item is MyImages.Image
    }

    override fun onBindViewHolder(
        item: MyImages.Image,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item.imageUrl)
    }


    class ViewHolder(
        view: ViewGroup
        //binding: ViewBinding,
    ) : RecyclerView.ViewHolder(view) {
        private val viewGroup = view as ViewGroup

        fun bind(imageUrl: String) {
            Glide.with(itemView)
                .load(imageUrl)
                .error(R.drawable.ic_baseline_cloud_off_24)
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .into((viewGroup.imageView))
        }
    }
}

