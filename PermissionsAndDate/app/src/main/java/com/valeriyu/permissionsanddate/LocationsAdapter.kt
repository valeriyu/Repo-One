package com.valeriyu.permissionsanddate

//import kotlinx.android.synthetic.main.item_time_locaion.view.*
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.valeriyu.permissionsanddate.databinding.ItemTimeLocaionBinding
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


class LocationsAdapter(var onItemClick: (position: Int) -> Unit) :
    ListAdapter<LocationTime, LocationsAdapter.Holder>(MessageDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsAdapter.Holder {
        //return LocationsAdapter.Holder(parent.inflate(R.layout.item_time_locaion), onItemClick)

        return Holder(
            com.valeriyu.permissionsanddate.databinding.ItemTimeLocaionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageDiffUtilCallback : DiffUtil.ItemCallback<LocationTime>() {
        override fun areItemsTheSame(oldItem: LocationTime, newItem: LocationTime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationTime, newItem: LocationTime): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(
        val binding: ItemTimeLocaionBinding,
        onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        private val formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
            .withZone(ZoneId.systemDefault())

        fun bind(message: LocationTime) {
   /*         var locationTxextView: TextView = view.findViewById(R.id.locationTxextView)
            var createdAtTextTView: TextView = view.findViewById(R.id.createdAtTextTView)
            var imageView: ImageView = view.findViewById(R.id.imageView)*/

            val locationTxextView: TextView = binding.locationTxextView
            val createdAtTextTView: TextView = binding.createdAtTextTView
            val imageView: ImageView = binding.imageView

            Glide.with(itemView)
                .load(message.url)
                .error(R.drawable.ic_baseline_cloud_off_24)
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .into(imageView)


            // createdAtTextTView.text = formatter.format(message.createdAt).trim() + "   ID = ${message.id}"
            createdAtTextTView.text = "Время:  " + formatter.format(message.createdAt).trim()
            // locationTxextView.text = message.loccation.toString()
            locationTxextView.text =
 """Широта = ${message.loccation.latitude}
Долгота = ${message.loccation.longitude}
Высота = ${message.loccation.altitude}
Скорость = ${message.loccation.speed}
Точность = ${message.loccation.accuracy}"""
        }
    }
}






