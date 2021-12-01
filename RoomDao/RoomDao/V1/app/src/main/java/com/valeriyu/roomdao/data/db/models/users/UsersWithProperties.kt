package com.valeriyu.roomdao.data.db.models.users

import androidx.room.*

data class UsersWithProperties(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = UsersContract.Columns.ID,
        entityColumn = UsersPropertiesContract.Columns.ID
    )
    val properties: UsersProperties?
)
