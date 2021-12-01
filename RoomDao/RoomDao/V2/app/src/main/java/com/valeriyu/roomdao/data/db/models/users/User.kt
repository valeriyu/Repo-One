package com.valeriyu.roomdao.data.db.models.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/*@Entity(
    tableName = "users",
    indices = [
        Index(value = arrayOf("phone"), unique = true)
    ]
)*/

@Entity(
    tableName = UsersContract.TABLE_NAME,
    indices = [
        Index(UsersContract.Columns.PHONE)
    ]
)

data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UsersContract.Columns.ID) val id: Long,
    @ColumnInfo(name = UsersContract.Columns.PHONE) val phone: String,
    @ColumnInfo(name = UsersContract.Columns.PASSWORD) val password: Int
    ,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "avatar") val avatar: String =""
)