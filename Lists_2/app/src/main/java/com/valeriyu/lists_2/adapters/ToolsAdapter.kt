package com.valeriyu.lists_2.adapters

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.valeriyu.lists_2.Linear.Tools


class ToolsAdapter(
    private val onItemClick: (position: Int) -> Unit
) : AsyncListDifferDelegationAdapter<Tools>(ToolsDiffUtilCallback()) {


    init {
        delegatesManager.addDelegate(ToolAdapterDelegate(onItemClick))
            .addDelegate(ProflToolAdapterDelegate(onItemClick))
            .addDelegate(SeparatorAdapterDelegate())
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


/*
class ToolsAdapter(
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val differ = AsyncListDiffer<Tools>(this, ToolsDiffUtilCallback())
    private val delegatesManager = AdapterDelegatesManager<List<Tools>>()

    init {
        delegatesManager.addDelegate(ToolAdapterDelegate(onItemClick))
            .addDelegate(ProflToolAdapterDelegate(onItemClick))
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(differ.currentList, position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }


    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return delegatesManager.onBindViewHolder(differ.currentList, position, holder)
    }

    fun updateTools(newTools: List<Tools>) {
        differ.submitList(newTools)
    }


    class ToolsDiffUtilCallback : DiffUtil.ItemCallback<Tools>() {
        override fun areItemsTheSame(oldItem: Tools, newItem: Tools): Boolean {
            return when {
                oldItem is Tools.Tool && newItem is Tools.Tool -> oldItem.id == newItem.id
                oldItem is Tools.ProflTool && newItem is Tools.ProflTool -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Tools, newItem: Tools): Boolean {
            return oldItem == newItem
        }
    }
}
*/


/*
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


        // private val _pictImageView: TextView = containerView.pictImageView

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
    ) : BaseHolder(view, onItemClick) {
        //init { }

        fun bind(tool: Tools.Tool) {
            bindMainInfo(tool.name, tool.pictLink, tool.description)
        }
    }

    class ProfToolHolder(
        view: View,
        onItemClick: (position: Int) -> Unit
    ) : BaseHolder(view, onItemClick) {

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
*/