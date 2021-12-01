package com.valeriyu.roomdao.app

import android.content.Context
import androidx.room.Room
import com.valeriyu.roomdao.data.db.MessangerDatabase

object Database {

    lateinit var instance: MessangerDatabase
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            MessangerDatabase::class.java,
            MessangerDatabase.DB_NAME
        )
            //.addMigrations(MIGRATION_1_2)
            //.fallbackToDestructiveMigration()
            .build()
    }
}