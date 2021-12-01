package com.valeriyu.notifications

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import com.valeriyu.notifications.db.MessageContract

@Entity(
    tableName = MessageContract.TABLE_NAME,
    indices = [
        Index("text"),
        Index("userId")
    ],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)

@JsonClass(generateAdapter = true)
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @Ignore
    var user: User? = null,
    var userId: Long = 0L,
    var created_at: Long = 0,
    var text: String = ""
)


/*
@JsonClass(generateAdapter = true)
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L,
    var userId: Long,
    var userName: String,
    var created_at: Long = 0,
    var text: String = ""
)*/
