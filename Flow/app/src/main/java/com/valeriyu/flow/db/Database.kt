package com.valeriyu.flow

import android.content.Context
import androidx.room.Room

object Database {

    lateinit var instance: MoviesDatabase
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            MoviesDatabase::class.java,
            MoviesDatabase.DB_NAME
        )
            //.addMigrations(MIGRATION_1_2)
            //.fallbackToDestructiveMigration()
            .build()
    }
}