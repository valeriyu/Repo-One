package com.valeriyu.flow.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valeriyu.flow.db.MoviesContract

@Entity(
    tableName = MoviesContract.TABLE_NAME,
    indices = [
        Index("imdbID", unique = true),
        Index("title"),
        Index("poster_cache_path")
    ]
)

@JsonClass(generateAdapter = true)
data class Movies(
    @PrimaryKey(autoGenerate = true)
    val _id: Long = 0,
    @Json(name = "Title")
    val title: String = "",
    @Json(name = "Year")
    val year: String = "",
    @Json(name = "imdbID")
    val imdbID: String,
    @Json(name = "Type")
    val type: String = "",
    @Json(name = "Poster")
    val poster: String = "",
    var poster_cache_path: String = ""
)