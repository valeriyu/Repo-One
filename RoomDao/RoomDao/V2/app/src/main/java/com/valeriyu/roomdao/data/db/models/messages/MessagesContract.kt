package com.valeriyu.roomdao.data.db.models.messages

object MessagesContract {
    const val TABLE_NAME = "messages"

    object Columns {
        const val ID = "id"
        const val SENDER = "sender"
        const val MESSAGE_STATUS = "message_status"
        const val CREATED_AT = "created_at"
        const val BODY = "body"
    }
}