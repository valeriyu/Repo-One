package com.valeriyu.roomdao.data.db.models.users

import androidx.room.*

@Entity(tableName = UsersPropertiesContract.TABLE_NAME,
    indices = [
        Index(
            UsersPropertiesContract.Columns.ID
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = [UsersContract.Columns.ID],
            childColumns = [UsersPropertiesContract.Columns.ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsersProperties(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UsersPropertiesContract.Columns.ID) val id: Long,
    @ColumnInfo(name = UsersPropertiesContract.Columns.NAME) val name: String,
    @ColumnInfo(name = UsersPropertiesContract.Columns.AVATAR) val avatar: String
)
