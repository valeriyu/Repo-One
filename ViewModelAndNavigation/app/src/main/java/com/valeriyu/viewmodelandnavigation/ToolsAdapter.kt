package com.valeriyu.viewmodelandnavigation

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter


class ToolsAdapter(
    private val onItemClick: (url: String) -> Unit,
    private val onItemLongClick: (position: Int) -> Unit

) : AsyncListDifferDelegationAdapter<Tools>(ToolsDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(ToolAdapterDelegate(onItemClick, onItemLongClick))
            .addDelegate(ProflToolAdapterDelegate(onItemClick, onItemLongClick))
    }


    class ToolsDiffUtilCallback : DiffUtil.ItemCallback<Tools>() {
        override fun areItemsTheSame(oldItem: Tools, newItem: Tools): Boolean {

            // Если url совпадают погда это однин и тот же эл. (вместо id)
            return when {
                oldItem is Tools.Tool && newItem is Tools.Tool -> oldItem.pictLink == newItem.pictLink
                oldItem is Tools.ProflTool && newItem is Tools.ProflTool -> oldItem.pictLink == newItem.pictLink
                else -> false
            }

        }

        override fun areContentsTheSame(oldItem: Tools, newItem: Tools): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}
