package com.valeriyu.roomdao.data.db.dao

import androidx.room.*
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users.UsersPropertiesContract
import com.valeriyu.roomdao.data.db.models.users.UsersProperties

@Dao
interface UserPropirtiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPropirties(props: List<UsersProperties>)

    @Update
    suspend fun updateUserPropirties(up: UsersProperties)
}
