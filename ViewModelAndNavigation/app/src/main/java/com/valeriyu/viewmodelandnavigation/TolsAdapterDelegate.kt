package com.valeriyu.viewmodelandnavigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.viewmodelandnavigation.databinding.ItemToolBinding

class ToolAdapterDelegate(
    private val onItemClick: (url: String) -> Unit,
    private val onItemLongClick: (position: Int) -> Unit
) :
    AbsListItemAdapterDelegate<Tools.Tool, Tools, ToolAdapterDelegate.ToolHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ToolHolder {
   /*     return ToolHolder(
            parent.inflate(R.layout.item_tool),
            onItemClick
        )  */

        return ToolAdapterDelegate.ToolHolder(
            ItemToolBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick, onItemLongClick
        )
    }

    override fun isForViewType(item: Tools, items: MutableList<Tools>, position: Int): Boolean {
        return item is Tools.Tool
    }


    override fun onBindViewHolder(
        item: Tools.Tool,
        holder: ToolHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ToolHolder(
        binding: ViewBinding,
        //view: View,
        onItemClick: (url: String) -> Unit,
        onItemLongClick: (position_: Int) -> Unit
    ) : BaseToolsHolder(binding, onItemClick, onItemLongClick) {
        //init { }

        fun bind(tool: Tools.Tool) {
            bindMainInfo(tool.name, tool.pictLink, tool.description)
        }
    }
}

