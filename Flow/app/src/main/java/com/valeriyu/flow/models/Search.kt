package com.valeriyu.flow.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valeriyu.flow.models.Movies

@JsonClass(generateAdapter = true)
data class Search(
    @Json(name = "Search")
    val search: List<Movies>?
)
