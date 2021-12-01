package com.valeriyu.lists_2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.lists_2.Linear.Tools
import com.valeriyu.lists_2.R
import com.valeriyu.lists_2.databinding.ItemToolBinding

import com.valeriyu.lists_2.inflate

class ToolAdapterDelegate(private val onItemClick: (position: Int) -> Unit) :
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
            ), onItemClick
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
        onItemClick: (position: Int) -> Unit
    ) : BaseToolsHolder(binding, onItemClick) {
        //init { }

        fun bind(tool: Tools.Tool) {
            bindMainInfo(tool.name, tool.pictLink, tool.description)
        }
    }
}

