package com.valeriyu.notifications.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RootModel(
    val to:String,
    val data:Any
)