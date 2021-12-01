package com.valeriyu.flow.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.valeriyu.flow.R
import com.valeriyu.flow.databinding.ItemMovieBinding
import com.valeriyu.flow.models.Movies


class MoviesListAdapter() : ListAdapter<Movies, MoviesListAdapter.Holder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilCallback() : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(
        val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movies) {

            binding.movieTextView.text =
                "Title: ${movie.title} \nimdbID: ${movie.imdbID} \nYear: ${movie.year} \n" +
                        "Type ${movie.type}"

            Glide.with(itemView)
                .load(
                    if (movie.poster_cache_path.isNotBlank()) movie.poster_cache_path
                    else movie.poster
                )
                .error(R.drawable.ic_baseline_cloud_off_24)
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .into(binding.posterImageView)
        }
    }
}