package com.valeriyu.contentprovider.custom_content_provider

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: Long,
    val name: String,
    val age: Int
)