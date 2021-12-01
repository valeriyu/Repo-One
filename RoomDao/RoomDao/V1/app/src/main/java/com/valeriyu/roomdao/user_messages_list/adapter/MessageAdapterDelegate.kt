package com.valeriyu.roomdao

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.valeriyu.roomdao.user_messages_list.MeesagesListItemsModels
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_message.*
import kotlinx.android.synthetic.main.item_message.view.*
import java.text.SimpleDateFormat


class MessageAdapterDelegate(
    private val onDeleteMessage: (Any) -> Unit
) : AbsListItemAdapterDelegate<MeesagesListItemsModels.Message, MeesagesListItemsModels, MessageAdapterDelegate.Holder>() {

    /* private val onDeleteMessageLiveData = MutableLiveData<Messages>()
     val onDeleteMessage: LiveData<Messages>
         get() = onDeleteMessageLiveData*/

    override fun isForViewType(
        item: MeesagesListItemsModels,
        items: MutableList<MeesagesListItemsModels>,
        position: Int
    ): Boolean {
        return item is MeesagesListItemsModels.Message
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return parent.inflate(R.layout.item_message).let {
            Holder(it, onDeleteMessage)
        }
    }

    override fun onBindViewHolder(
        item: MeesagesListItemsModels.Message,
        holder: Holder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class Holder(
        override val containerView: View,
        private val onDeleteMessage: (Any) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        var message: Any? = null
        var df: SimpleDateFormat = SimpleDateFormat("dd-MM-yy HH:mm:ss")

        init {
            containerView.closeMsgImg.setOnClickListener {
                message?.let { m -> onDeleteMessage(m) }
            }
        }

        fun bind(msg: MeesagesListItemsModels.Message) {
            //messageTextView.text = msg.value.body
         /*   message = Messages(
                id = msg.value.id,
                sender = msg.value.sender,
                created_at = msg.value.created_at,
                body = msg.value.body
            )*/
            message = msg.value.id


            messageTextView.text = """
 id = ${msg.value.id}     ${msg.value.sender_name} -> ${msg.value.receiver_name}     ${df.format(msg.value.created_at)}
 Текст сообщения:
 ${msg.value.body}
 """.replace("\"", "")
        }
    }
}
