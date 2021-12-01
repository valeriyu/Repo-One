package com.valeriyu.scopedstorage

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.scopedstorage.databinding.ItemMovieBinding
import java.io.File


class MovieAdapterDelegate(
    private val onClick: (Long, Int) -> Unit
) : AbsListItemAdapterDelegate<Movie, Movie, MovieAdapterDelegate.Holder>() {

    override fun isForViewType(item: Movie, items: MutableList<Movie>, position: Int): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return parent.inflate(ItemMovieBinding::inflate).let { Holder(it, onClick) }
    }

    override fun onBindViewHolder(item: Movie, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        private val binding: ItemMovieBinding,
        onClick: (Long, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var movieId: Long? = null

        init {
            binding.deleteButton.setOnClickListener {
                onClick(movieId!!, R.id.deleteButton)
            }

            binding.starredButton.setOnClickListener {
                onClick(movieId!!, R.id.starredButton)
            }

            binding.trashedButton.setOnClickListener {
                onClick(movieId!!, R.id.trashedButton)
            }
        }

        fun bind(item: Movie) {
            movieId = item.id
            with(binding) {
                nameTextView.text = item.name
                sizeTextView.text = "${item.size} bytes"
                pathTextView.text = item.path
                    Glide.with(imageView.context)
                        //.asBitmap()
                        .load(item.uri)
                        .placeholder(R.drawable.ic_image)
                        .into(imageView)
                if (item.favorite) {
                    binding.starredButton.setImageResource(R.drawable.ic_baseline_star_rate_24)
                } else {
                    binding.starredButton.setImageResource(R.drawable.ic_baseline_star_outline_24)
                }

                if (item.trashed) {
                    binding.trashedButton.setImageResource(R.drawable.ic_delete)
                } else {
                    binding.trashedButton.setImageResource(R.drawable.ic_baseline_delete_outline_24)
                }
            }
        }
    }
}