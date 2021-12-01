package com.valeriyu.notifications.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valeriyu.notifications.Message
import com.valeriyu.notifications.User
import com.valeriyu.notifications.models.Promotions


@Database(
    entities = [
        User::class,
        Message::class,
        Promotions::class,

    ], version = NotificationsDatabase.DB_VERSION, exportSchema = true
)

abstract class NotificationsDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "notifications_database"
    }
}


