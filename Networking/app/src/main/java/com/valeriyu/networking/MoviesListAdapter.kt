package com.valeriyu.networking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillbox.multithreading.networking.Movie
import com.valeriyu.networking.databinding.ItemMovieBinding

class MoviesListAdapter() : ListAdapter<Movie, MoviesListAdapter.Holder>(MoviesListAdapter.DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListAdapter.Holder {
        return Holder(
                ItemMovieBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: MoviesListAdapter.Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilCallback() : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(
            val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {

            binding.movieTextView.text = movie.toString()
                    .replace("Movie", "")
                    .replace("(","")
                    .replace(")","")
                    .replace(",","\n")
                    .replace("=",":  ")
                    .trim()

            //binding.movieTextView.text = "Фильм: " +  movie.title +"\nГод: " +  movie.year
            //var info = movie.toString().
            Glide.with(itemView)
                    .load(movie.poster)
                    .error(R.drawable.ic_baseline_cloud_off_24)
                    .placeholder(R.drawable.ic_baseline_cloud_download_24)
                    .into(binding.posterImageView)
        }
    }
}