package com.valeriyu.roomdao.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.valeriyu.roomdao.data.db.models.attachments.Attachments
import com.valeriyu.roomdao.data.db.models.attachments.MessagesWithAttachments
import com.valeriyu.roomdao.data.db.models.messages.*
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users_messages.UsersMessages
import com.valeriyu.roomdao.getDistinct

@Dao
interface MessagesDao {
    @Query("SELECT  * FROM messages m ORDER BY m.created_at DESC LIMIT 1")
    fun getLastMessage(): LiveData<List<Messages>>

    @Delete
    suspend fun deleteAttachment(attach: Attachments)

    @Delete
    suspend fun deleteAttachments(attachList: List<Attachments>)

    @Transaction
    @Query(
        """
        SELECT DISTINCT
         s.name sender_name,
         r.name receiver_name,
         m.*
          FROM messages m
          LEFT JOIN users_messages um ON um.message = m.id
            LEFT JOIN users s ON m.sender = s.id
            LEFT JOIN users r ON um.receiver = r.id
            WHERE m.sender = :id OR m.id IN
             (SELECT message FROM users_messages WHERE receiver = :id)
              AND
              m.sender <> um.receiver
            ORDER BY created_at
            
                 """
    )
    suspend fun getAllUserMessagesWithAttachments(id: Long): List<MessagesWithAttachments>

    @Query("SELECT * FROM ${MessagesContract.TABLE_NAME} WHERE ${MessagesContract.Columns.SENDER} = :id")
    suspend fun getInputtUserMessages(id: Long): Messages

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(msg: Messages)

    @Query("SELECT max(${MessagesContract.Columns.ID}) FROM ${MessagesContract.TABLE_NAME}")
    suspend fun geMaxId(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsersMessages(um: UsersMessages)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(att: List<Attachments>)

    @Delete
    suspend fun deleteMessage(msg: Messages)

    @Query("DELETE FROM messages WHERE id = :msgId")
    suspend fun deleteMessageById(msgId: Long)

    @Query(
        """
        DELETE FROM messages 
        WHERE id NOT IN (SELECT um.message  FROM users_messages um)
        """
    )
    suspend fun deleteMessagesWihoutReceiver()

}