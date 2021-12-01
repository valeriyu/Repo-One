package com.valeriyu.roomdao.data.db.models.attachments

import androidx.room.Embedded
import androidx.room.Relation
import com.valeriyu.roomdao.data.db.models.messages.MeesgesWithSendersAndReceiversNames
import com.valeriyu.roomdao.data.db.models.messages.MessagesContract


data class MessagesWithAttachments(
    @Embedded
    val message: MeesgesWithSendersAndReceiversNames,

    @Relation(
        parentColumn = MessagesContract.Columns.ID,
        entityColumn = AttachmentsContract.Columns.MESSAGE
    )
    val attachments: List<Attachments>
)
