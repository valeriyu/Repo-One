package com.valeriyu.roomdao.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valeriyu.roomdao.data.db.dao.MessagesDao
import com.valeriyu.roomdao.data.db.dao.UserDao
import com.valeriyu.roomdao.data.db.models.attachments.Attachments
import com.valeriyu.roomdao.data.db.models.messages.Messages
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users_messages.UsersMessages

@Database(
    entities = [
        User::class,
        //UsersProperties::class,
        Messages::class,
        UsersMessages::class,
        Attachments::class
        //,MessagesWithAttachments::class

    ], version = MessangerDatabase.DB_VERSION, exportSchema = true
)
abstract class MessangerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    //abstract fun userPropirtiesDao(): UserPropirtiesDao
    abstract fun messagesDao(): MessagesDao
    //abstract fun _messagesDao(): _MessagesDao

    companion object {
        const val DB_VERSION = 2
        const val DB_NAME = "messager-database"
    }
}


