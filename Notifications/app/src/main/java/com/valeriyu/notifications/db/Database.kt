package com.valeriyu.notifications.app

import android.content.Context
import androidx.room.Room

object Database {

    lateinit var instance: NotificationsDatabase
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            NotificationsDatabase::class.java,
            NotificationsDatabase.DB_NAME
        )
            //.addMigrations(MIGRATION_1_2)
            //.fallbackToDestructiveMigration()
            .build()
    }
}