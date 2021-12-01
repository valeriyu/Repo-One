package com.valeriyu.roomdao.data.db

import androidx.room.migration.Migration
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.valeriyu.roomdao.app.Database
import timber.log.Timber

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Timber.d("migration 1-2 start")

        database.execSQL(
            """
        CREATE TABLE IF NOT EXISTS users_tmp (
        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
         `phone` TEXT NOT NULL, `password` INTEGER NOT NULL,
         `name` TEXT NOT NULL,
         `avatar` TEXT NOT NULL)
           """
        )

        database.execSQL(
            """
        insert into users_tmp
        select u.id, u.phone, u.password, up.name, up.avatar
        from users u left join users_properties up on u.id = up.id
            """
        )

        database.execSQL(
            """
        drop table users
            """
        )

     /*   database.execSQL(
            """
        CREATE TABLE IF NOT EXISTS users (
        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
         `phone` TEXT NOT NULL,
         `password` INTEGER NOT NULL
          ,
         `name` TEXT NOT NULL,
         `avatar` TEXT NOT NULL
            )
           """
        )*/

        database.execSQL(
            """
                ALTER TABLE users_tmp RENAME TO users
             """
        )

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_users_phone` ON users (`phone`)")

  /*      database.execSQL(
            """
        insert into users select id, phone, password, name, avatar from users_tmp
            """
        )*/

     /*   database.execSQL(
            """
        drop table users_tmp
            """
        )*/

        database.execSQL(
            """
        drop table users_properties
            """
        )

        Timber.d("migration 1-2 success")
    }
}