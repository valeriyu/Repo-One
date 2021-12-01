package com.valeriyu.roomdao.data.db.models.users_messages

object UsersMessagesContract {
    const val TABLE_NAME = "users_messages"

    object Columns {
        const val ID = "id"
        const val RECEIVER = "receiver"
        const val MESSAGE = "message"
    }
}