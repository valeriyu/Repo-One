package com.valeriyu.roomdao.data.db.models.attachments

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valeriyu.roomdao.data.db.models.messages.Messages
import com.valeriyu.roomdao.data.db.models.messages.MessagesContract
import com.valeriyu.roomdao.data.db.models.users.UsersContract

@Entity(
    tableName = AttachmentsContract.TABLE_NAME,
    indices = [
        Index(AttachmentsContract.Columns.MESSAGE)
    ],
            foreignKeys = [
            androidx.room.ForeignKey(
                entity = Messages::class,
                parentColumns = [MessagesContract.Columns.ID],
                childColumns = [AttachmentsContract.Columns.MESSAGE]
                ,
                onDelete = CASCADE
            )
    ]
)

data class Attachments(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AttachmentsContract.Columns.ID) val id: Long,
    @ColumnInfo(name = AttachmentsContract.Columns.MESSAGE) val message: Long,
    @ColumnInfo(name = AttachmentsContract.Columns.CONTENT) val content: String
)
