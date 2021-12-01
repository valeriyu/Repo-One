package com.valeriyu.roomdao

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.roomdao.ui.user_messages_list.MeesagesListItemsModels
import com.valeriyu.roomdao.ui.user_messages_list.adapter.AttachmentsListAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.attachments_list.view.*

@SuppressLint("WrongConstant")
class AttachmentsAdapterDelegate(
    private val onDeleteAttachment: (Any) -> Unit
) : AbsListItemAdapterDelegate<MeesagesListItemsModels.Attachments, MeesagesListItemsModels, AttachmentsAdapterDelegate.Holder>() {

    override fun isForViewType(
        item: MeesagesListItemsModels,
        items: MutableList<MeesagesListItemsModels>,
        position: Int
    ): Boolean {
        return item is MeesagesListItemsModels.Attachments
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return parent.inflate(R.layout.attachments_list).let {
            Holder(it)
        }
    }

    override fun onBindViewHolder(
        item: MeesagesListItemsModels.Attachments,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
      holder.bind(item, onDeleteAttachment)
    }

    class Holder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        var nestedList: RecyclerView? = null

      fun bind(
            attachments: MeesagesListItemsModels.Attachments,
            onDeleteAttachment: (Any) -> Unit
        ) {
            nestedList = itemView.attachmentsList
            nestedList?.apply {
                layoutManager =
                    LinearLayoutManager(nestedList?.context, LinearLayout.HORIZONTAL, false)
                adapter = AttachmentsListAdapter(attachments.attachList){
                    onDeleteAttachment(it)
                }
            }
        }
    }
}
