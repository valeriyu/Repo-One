package com.valeriyu.roomdao.ui.user_messages_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.valeriyu.roomdao.R
import com.valeriyu.roomdao.data.db.models.attachments.Attachments
import kotlinx.android.synthetic.main.item_attachment.view.*


class AttachmentsListAdapter(
    private val attachments: List<Attachments>,
    private var onDeleteAttachment: (Any) -> Unit
) :
    RecyclerView.Adapter<AttachmentsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachment, parent, false)
        return ViewHolder(v, onDeleteAttachment)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val atchPos = attachments[position]
        holder.attachment = attachments[position]
        Glide.with(holder.itemView)
            .load(atchPos.content)
            //.transform(CircleCrop())
            .placeholder(R.drawable.ic_emoji)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return attachments.size
    }

    class ViewHolder(
        itemView: View,
        onDeleteAttachment: (Any) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.imageView
        var attachment: Any? = null

        init {
            itemView.delAttachmentBtn.setOnClickListener {
                onDeleteAttachment(attachment as Attachments)
            }
            itemView.imageView.setOnClickListener {
               //TODO
            }
        }
    }
}
