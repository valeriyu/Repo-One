package com.valeriyu.roomdao.data.db.models.messages

import androidx.room.*
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users.UsersContract
import com.valeriyu.roomdao.data.db.models.users_messages.UsersMessages
import com.valeriyu.roomdao.data.db.models.users_messages.UsersMessagesContract
import java.util.*

@Entity(
    tableName = MessagesContract.TABLE_NAME,
    indices = [
        Index(
            MessagesContract.Columns.SENDER
        )

    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = [UsersContract.Columns.ID],
            childColumns = [MessagesContract.Columns.SENDER],
            onDelete = ForeignKey.CASCADE
        )
      /*  ,
        ForeignKey(
            entity = UsersMessages::class,
            parentColumns = [UsersMessagesContract.Columns.MESSAGE],
            childColumns = [MessagesContract.Columns.ID],
            onDelete = ForeignKey.CASCADE
        )*/
    ]
)

//@TypeConverters(MessageStatuses::class)
data class Messages(
    //@Ignore
    //@ColumnInfo(name = "sender_name") val sender_name: String?,

    //@Ignore
    //@ColumnInfo(name = "receiver_name") val receiver_name: String?,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MessagesContract.Columns.ID) val id: Long,
    @ColumnInfo(name = MessagesContract.Columns.SENDER) val sender: Long,
    @field:TypeConverters(Converters::class)
    @ColumnInfo(name = MessagesContract.Columns.MESSAGE_STATUS) val message_status: MessageStatuses? = null,
    @field:TypeConverters(Converters::class)
    @ColumnInfo(name = MessagesContract.Columns.CREATED_AT) val created_at: Date,
    @ColumnInfo(name = MessagesContract.Columns.BODY) val body: String
)


