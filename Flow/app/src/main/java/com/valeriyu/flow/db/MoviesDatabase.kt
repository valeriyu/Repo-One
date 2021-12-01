package com.valeriyu.flow

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valeriyu.flow.models.Movies
import com.valeriyu.flow.db.MoviesDao


@Database(
    entities = [
        Movies::class,

    ], version = MoviesDatabase.DB_VERSION, exportSchema = true
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "movies-database"
    }
}


