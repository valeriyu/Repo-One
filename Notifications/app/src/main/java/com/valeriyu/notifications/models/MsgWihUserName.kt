package com.valeriyu.notifications

import androidx.room.*
import com.squareup.moshi.JsonClass
import com.valeriyu.notifications.db.MessageContract
import com.valeriyu.notifications.models.Converters

data class MsgWihUserName(
    var id: Long = 0L,
    var userName: String,
    //@field:TypeConverters(Converters::class)
    var created_at: String = "",
    var text: String = ""
)



