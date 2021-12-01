package com.valeriyu.lists_2.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.lists_2.Linear.Tools
import com.valeriyu.lists_2.R
import com.valeriyu.lists_2.inflate
import kotlinx.android.extensions.LayoutContainer

class SeparatorAdapterDelegate :
    AbsListItemAdapterDelegate<Tools.Separator, Tools, SeparatorAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent.inflate((R.layout.item_separator)))
    }

    override fun isForViewType(
        item: Tools,
        items: MutableList<Tools>,
        position: Int
    ): Boolean {
        return item is Tools.Separator
    }

    override fun onBindViewHolder(
        item: Tools.Separator,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        //holder.bind(item.imageUrl)
    }


    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind() {

        }
    }
}