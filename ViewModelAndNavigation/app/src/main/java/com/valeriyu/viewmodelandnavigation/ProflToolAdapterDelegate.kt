package com.valeriyu.viewmodelandnavigation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.viewmodelandnavigation.databinding.ItemProfToolBinding

class ProflToolAdapterDelegate(
    private val onItemClick: (url: String) -> Unit,
    private val onItemLongClick: (position: Int) -> Unit

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
            ), onItemClick, onItemLongClick
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
        binding: ItemProfToolBinding,
        onItemClick: (url: String) -> Unit,
        onItemLongClick: (position: Int) -> Unit
    ) : BaseToolsHolder(binding, onItemClick, onItemLongClick) {

        // private val weightTextView: TextView = view.findViewById(R.id.weightTextView)
        private val weightTextView: TextView = binding.weightTextView

        fun bind(tool: Tools.ProflTool) {
            bindMainInfo(tool.name, tool.pictLink, tool.description)
            weightTextView.text = "Вес ${tool.weight} кг."
        }
    }
}

