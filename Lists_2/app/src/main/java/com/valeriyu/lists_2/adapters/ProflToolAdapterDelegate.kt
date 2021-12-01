package com.valeriyu.lists_2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.lists_2.Linear.Tools
import com.valeriyu.lists_2.R
import com.valeriyu.lists_2.databinding.ItemProfToolBinding

class ProflToolAdapterDelegate(
    private val onItemClick: (position: Int) -> Unit

) :
    AbsListItemAdapterDelegate<Tools.ProflTool, Tools, ProflToolAdapterDelegate.ProfToolHolder>() {

    override fun isForViewType(item: Tools, items: MutableList<Tools>, position: Int): Boolean {
        return item is Tools.ProflTool
    }

    override fun onCreateViewHolder(parent: ViewGroup): ProfToolHolder {
           return ProflToolAdapterDelegate.ProfToolHolder(
            ItemProfToolBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )
    }

    override fun onBindViewHolder(
        item: Tools.ProflTool,
        holder: ProfToolHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class ProfToolHolder(
        binding: ViewBinding,
        onItemClick: (position: Int) -> Unit
    ) : BaseToolsHolder(binding, onItemClick) {
        private val weightTextView: TextView = view.findViewById(R.id.weightTextView)

        fun bind(tool: Tools.ProflTool) {
            bindMainInfo(tool.name, tool.pictLink, tool.description)
            weightTextView.text = "Вес ${tool.weight} кг."
        }
    }
}

