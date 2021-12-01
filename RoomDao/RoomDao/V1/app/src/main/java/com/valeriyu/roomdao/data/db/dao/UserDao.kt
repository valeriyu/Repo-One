package com.valeriyu.roomdao.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users.UsersContract
import com.valeriyu.roomdao.data.db.models.users.UsersWithProperties
import com.valeriyu.roomdao.getDistinct

@Dao
interface UserDao {

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun saveUserWithPropierties(uwp: UsersWithProperties)

    @Query("SELECT max(id) FROM ${UsersContract.TABLE_NAME}")
    suspend fun getMaxId(): Long

    @Query("SELECT * FROM ${UsersContract.TABLE_NAME} WHERE ${UsersContract.Columns.ID} = :userId")
    suspend fun getUserWithPropertiesById(userId: Long): UsersWithProperties?

    @Transaction
    @Query("SELECT * FROM ${UsersContract.TABLE_NAME}")
    suspend fun getAllUsersWithProperties(): List<UsersWithProperties>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Query("SELECT * FROM ${UsersContract.TABLE_NAME}")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM ${UsersContract.TABLE_NAME} WHERE ${UsersContract.Columns.ID} = :userId")
    suspend fun getUserById(userId: Long): User?

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun removeUser(user: User)

    @Query("DELETE FROM ${UsersContract.TABLE_NAME} WHERE ${UsersContract.Columns.ID} = :userId")
    suspend fun removeUserById(userId: Long)

    @Query("DELETE FROM ${UsersContract.TABLE_NAME}")
    suspend fun removeAll()

}
