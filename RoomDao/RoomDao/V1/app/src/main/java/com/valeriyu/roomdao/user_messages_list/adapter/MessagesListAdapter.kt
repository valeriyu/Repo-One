package com.valeriyu.roomdao

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.valeriyu.roomdao.user_messages_list.MeesagesListItemsModels


class MessagesListAdapter(
    onDeleteMessage: (Any) -> Unit,
    onDeleteAttachment: (Any) -> Unit
) : AsyncListDifferDelegationAdapter<MeesagesListItemsModels>(UserDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(MessageAdapterDelegate(onDeleteMessage))
        delegatesManager.addDelegate(AttachmentsAdapterDelegate(onDeleteAttachment))
    }

    class UserDiffUtilCallback : DiffUtil.ItemCallback<MeesagesListItemsModels>() {
        override fun areItemsTheSame(
            oldItem: MeesagesListItemsModels,
            newItem: MeesagesListItemsModels
        ): Boolean {
           /* return when {
                oldItem is MeesagesListItemsModels.Message && newItem is MeesagesListItemsModels.Message -> oldItem.value.id == newItem.value.id
                else -> false
            }*/
            return false
        }

    override fun areContentsTheSame(
        oldItem: MeesagesListItemsModels,
        newItem: MeesagesListItemsModels
    ): Boolean {
        //return oldItem == newItem
        return false
    }
}

}
