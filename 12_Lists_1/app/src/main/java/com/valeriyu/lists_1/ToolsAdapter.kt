package com.valeriyu.lists_1

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ToolsAdapter(
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var tools: List<Tools> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TOOL -> ToolHolder(parent.inflate(R.layout.item_tool), onItemClick)
            TYPE_PROF_TOOL -> ProfToolHolder(
                parent.inflate(R.layout.item_prof_tool),
                onItemClick
            )
            else -> error("Incorrect viewType=$viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (tools[position]) {
            is Tools.Tool -> TYPE_TOOL
            is Tools.ProflTool -> TYPE_PROF_TOOL
        }
    }

    override fun getItemCount(): Int = tools.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ToolHolder -> {
                val item = tools[position].let { it as? Tools.Tool }
                    ?: error("Tool at position = $position is not a tool")
                holder.bind(item)
            }

            is ProfToolHolder -> {
                val item = tools[position].let { it as? Tools.ProflTool }
                    ?: error("ProfTool at position = $position is not a profTool")
                holder.bind(item)
            }

            else -> error("Incorrect view holder = $holder")
        }
    }

    fun updateTools(newTools: List<Tools>) {
        tools = newTools
    }

    abstract class BaseHolder(
        view: View,
        onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        private val pictImageView: ImageView = view.findViewById(R.id.pictImageView)

        init {
            view.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        protected fun bindMainInfo(
            name: String,
            pictLink: String,
            description: String
        ) {
            nameTextView.text = name
            descriptionTextView.text = description

            Glide.with(itemView)
                .load(pictLink)
                .error(R.drawable.ic_baseline_cloud_off_24)
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .into(pictImageView)
        }

    }

    class ToolHolder(
        view: View,
        onItemClick: (position: Int) -> Unit
    ) : BaseHolder( view, onItemClick) {
        //init { }

        fun bind(tool: Tools.Tool) {
            bindMainInfo(tool.name, tool.pictLink, tool.description)
        }
    }

    class ProfToolHolder(
        view: View,
        onItemClick: (position: Int) -> Unit
    ) : BaseHolder( view, onItemClick) {

        private val weightTextView: TextView = view.findViewById(R.id.weightTextView)

        fun bind(tool: Tools.ProflTool) {
            bindMainInfo(tool.name, tool.pictLink, tool.description)
            weightTextView.text = "Вес ${tool.weight} кг."
        }

    }

    companion object {
        private const val TYPE_TOOL = 1
        private const val TYPE_PROF_TOOL = 2
    }
}