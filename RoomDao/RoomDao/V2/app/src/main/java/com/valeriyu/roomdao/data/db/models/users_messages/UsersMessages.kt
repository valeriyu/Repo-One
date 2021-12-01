package com.valeriyu.roomdao.data.db.models.users_messages

import androidx.room.*
import com.valeriyu.roomdao.data.db.models.messages.Messages
import com.valeriyu.roomdao.data.db.models.messages.MessagesContract
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users.UsersContract

@Entity(
    tableName = UsersMessagesContract.TABLE_NAME,
    indices = [
        Index(UsersMessagesContract.Columns.RECEIVER),
        Index(UsersMessagesContract.Columns.MESSAGE)
    ],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = User::class,
            parentColumns = [UsersContract.Columns.ID],
            childColumns = [UsersMessagesContract.Columns.RECEIVER],
            onDelete = ForeignKey.CASCADE
        )
    /*    ,
        androidx.room.ForeignKey(
            entity = Messages::class,
            parentColumns = [MessagesContract.Columns.ID],
            childColumns = [UsersMessagesContract.Columns.MESSAGE],
            onDelete = ForeignKey.CASCADE
        )*/

    ]
)

data class UsersMessages(
    //@PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = UsersMessagesContract.Columns.ID) val id: Long,

    @PrimaryKey
    @ColumnInfo(name = UsersMessagesContract.Columns.MESSAGE) val message: Long,
    @ColumnInfo(name = UsersMessagesContract.Columns.RECEIVER) val receiver: Long
)