package com.valeriyu.notifications.app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.valeriyu.notifications.Message
import com.valeriyu.notifications.MsgWihUserName
import com.valeriyu.notifications.User
import com.valeriyu.notifications.models.Promotions
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(list: List<Message>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(msg: Message)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserUser(user: User?)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromotions(list: List<Promotions>)

    @Query(
        """
    select m.*, userName
    from messages m, users u
    where m.userId = u.id
    """
    )
    fun observeMessages(): Flow<List<MsgWihUserName>>


    /*@Query("select * from messages")
      fun observeMovies(): Flow<List<Message>>*/

}