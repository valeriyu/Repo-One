package com.valeriyu.roomdao.data.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users.UsersContract

@Dao
interface UserDao {

    @Query("SELECT max(id) FROM ${UsersContract.TABLE_NAME}")
    suspend fun getMaxId(): Long


    @Query("SELECT * FROM ${UsersContract.TABLE_NAME} WHERE ${UsersContract.Columns.ID} = :userId")
    suspend fun getUserWithPropertiesById(userId: Long): User?

    @Query("SELECT * FROM ${UsersContract.TABLE_NAME}")
    suspend fun getAllUsersWithProperties(): List<User>

    @Query("SELECT * FROM ${UsersContract.TABLE_NAME}")
    fun getUsersObservable(): LiveData<List<User>>

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
