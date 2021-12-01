package com.valeriyu.notifications

import android.net.Uri
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "users",
    indices = [
        Index("userName")
    ]
)

@JsonClass(generateAdapter = true)
data class User(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L,
    var userName: String,
)