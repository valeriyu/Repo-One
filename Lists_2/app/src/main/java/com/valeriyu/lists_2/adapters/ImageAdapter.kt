package com.valeriyu.lists_2.adapters

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.valeriyu.lists_2.Grid.MyImages
import com.valeriyu.lists_2.Linear.Tools

class ImageAdapter() : AsyncListDifferDelegationAdapter<MyImages>(
    ImageDiffUtilCallback()
) {

    init {
        delegatesManager.addDelegate(ImageAdapterDelegate())
    }


    class ImageDiffUtilCallback : DiffUtil.ItemCallback<MyImages>() {
        override fun areItemsTheSame(oldItem: MyImages, newItem: MyImages): Boolean {
            return (oldItem as MyImages.Image).imageUrl == (newItem as MyImages.Image).imageUrl
        }

        override fun areContentsTheSame(oldItem: MyImages, newItem: MyImages): Boolean {
            return oldItem == newItem
        }
    }
}


/*
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var images: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent.inflate(
                R.layout.item_image
            )
        )
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    fun setImages(newImages: List<String>) {
        images = newImages
        notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(imageUrl: String) {
            Glide.with(itemView)
                .load(imageUrl)
                .error(R.drawable.ic_baseline_cloud_off_24)
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .into(imageView)
        }
    }
}
 */