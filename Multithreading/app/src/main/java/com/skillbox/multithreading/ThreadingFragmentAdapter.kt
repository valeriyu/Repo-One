package com.skillbox.multithreading

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skillbox.multithreading.databinding.ItemMovieBinding
import com.skillbox.multithreading.networking.Movie



class MoviesAdapter(): ListAdapter<Movie,  MoviesAdapter.Holder>(MoviesAdapter.DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.Holder {
        return Holder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MoviesAdapter.Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffUtilCallback(): DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(
        val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTextView.text = "Фильм: " +  movie.title +"\nГод: " +  movie.year
        }
    }
}


