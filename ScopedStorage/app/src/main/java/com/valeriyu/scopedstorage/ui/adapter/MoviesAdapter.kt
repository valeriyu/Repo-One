package com.valeriyu.scopedstorage.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.valeriyu.scopedstorage.Movie
import com.valeriyu.scopedstorage.MovieAdapterDelegate


class MoviesAdapter(
    onClick: (Long, Int) -> Unit
): AsyncListDifferDelegationAdapter<Movie>(ImageDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(MovieAdapterDelegate(onClick))
    }

    class ImageDiffUtilCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

}