package com.valeriyu.notifications.models

import com.squareup.moshi.JsonClass
import com.valeriyu.notifications.Message

@JsonClass(generateAdapter = true)
data class DataModel(
    val type:String = "chat",
    val data: Any
)
