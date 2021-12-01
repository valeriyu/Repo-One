package com.valeriyu.roomdao.data.db.models.messages

import androidx.room.TypeConverters
import java.util.*

data class MeesgesWithSendersAndReceiversNames(
    val sender_name: String?,
    val receiver_name:String?,
    val id: Long,
    val sender: Long,
    val message_status: String,
    @field:TypeConverters(Converters::class)
    val created_at: Date,
    val body: String
)
