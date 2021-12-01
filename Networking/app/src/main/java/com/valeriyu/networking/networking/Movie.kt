package com.skillbox.multithreading.networking

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("imbdID")
    val id: String,
    @SerializedName("Title")
    val title: String = "",
    @SerializedName("Year")
    val year: String = "",
    @SerializedName("Type")
    val type: String = "",
    @SerializedName("Poster")
    val poster: String = ""
)