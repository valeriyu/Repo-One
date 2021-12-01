package com.valeriyu.notifications.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "promotions",
    indices = [
        Index("title")
    ]
)

@JsonClass(generateAdapter = true)
data class Promotions(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String,
    var description: String,
    var imageUrl: String? = null
)