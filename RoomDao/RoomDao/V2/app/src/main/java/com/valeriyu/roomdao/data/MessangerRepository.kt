package com.valeriyu.roomdao

import android.content.Context
import androidx.room.withTransaction
import com.valeriyu.roomdao.app.Database
import com.valeriyu.roomdao.data.db.dao.UserDao
import com.valeriyu.roomdao.data.db.models.attachments.Attachments
import com.valeriyu.roomdao.data.db.models.attachments.MessagesWithAttachments
import com.valeriyu.roomdao.data.db.models.messages.MessageStatuses
import com.valeriyu.roomdao.data.db.models.messages.Messages
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.data.db.models.users_messages.UsersMessages
import java.util.*


class MessangerRepository(cont: Context) {

    private var context = cont
    private val _userDao = Database.instance.userDao()
    private val messagesDao = Database.instance.messagesDao()

    val userDao:UserDao
    get() = _userDao

    private val images = listOf(
        "https://images.unsplash.com/photo-1588064719685-bd29437024f4?ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80",
        "https://thumbs.dreamstime.com/z/black-white-vertical-new-york-flatiron-building-stands-right-heart-manhattan-intersection-two-famous-nyc-landmarks-45486075.jpg",
        "https://images.unsplash.com/photo-1473968512647-3e447244af8f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2250&q=80",
        "https://images.unsplash.com/photo-1568198473832-b6b0f46328c1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2192&q=80",
        "https://images.unsplash.com/photo-1580221465362-963dd392df37?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2234&q=80",
        "https://images.unsplash.com/photo-1555356342-fa18a5da123f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1600&q=80"
    )

    private var avatars = listOf(
        "https://cdn.pixabay.com/photo/2014/04/03/10/32/businessman-310819_1280.png",
        "https://png.pngtree.com/png-clipart/20190922/original/pngtree-business-male-user-avatar-vector-png-image_4774078.jpg",
        "https://png.pngtree.com/png-clipart/20190924/original/pngtree-female-user-avatars-flat-style-women-profession-vector-png-image_4822944.jpg",
        "https://cdn.pixabay.com/photo/2014/04/03/10/32/user-310807_1280.png"
    )

    private var striveTo = listOf(
        "Стремись",
        "Пробивайся",
        "Двигайся",
        "Иди",
        "Проламывайся",
        "Беги"
    )

    private var result = listOf(
        "к успеху",
        "к вершине",
        "к отношениям",
        "к деньгам",
        "к мечтам",
        "к бизнесу",
        "к детям",
        "к здоровью",
        "к цели",
        "к любви",
        "к красивой жизни",
        "к cпортивному телу",
        "к балансу"
    )

    private var timeOccasion = listOf(
        "сейчас",
        "сегодня",
        "каждый день",
        "день изо дня",
        "ежедневно",
        "шаг за шагом",
        "по чуть-чуть"
    )


    suspend fun addMessage(sId: Long, rId: Long) {
        createMessage(
            sId,
            rId,
            getRndMessage(),
            List<String>((0..images.size).random()) { images.random() }.toSet().toList()
        )
    }

    suspend fun saveUserWithPropierties(uwp: User) {
        Database.instance.withTransaction {
            userDao.insertUsers(listOf(uwp))
        }
    }


    suspend fun removeUser(userId: Long) {
        Database.instance.withTransaction {
            userDao.removeUserById(userId)
            messagesDao.deleteMessagesWihoutReceiver()
        }
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun getUserMessages(id: Long): List<MessagesWithAttachments> {
        return messagesDao.getAllUserMessagesWithAttachments(id)
    }

    suspend fun getAllUsersWithProperties(): List<User> {
        return userDao.getAllUsersWithProperties()
    }


    suspend fun getUserWithPropertiesById(id: Long): User? {
        return userDao.getUserWithPropertiesById(id)
    }

    suspend fun deleteMessage(msg: Messages) {
        messagesDao.deleteMessage(msg)
    }

    suspend fun deleteAttachment(attach: Attachments) {
        messagesDao.deleteAttachment(attach)
    }

// init =================================================================================

    suspend fun initDB() {
        val sharedPrefs =
            context.getSharedPreferences(FIRST_START_SHARED_PREFS, Context.MODE_PRIVATE)

        if (sharedPrefs.getInt(FIRST_START_FLAG, -1) == -1) {
            sharedPrefs.edit().putInt(FIRST_START_FLAG, 1).apply()
        } else {
            return
        }
        initUsers()
        initMessages()
    }

    suspend fun initMessages() {
        var users = userDao.getAllUsers()
        users.forEach { s ->
            users.forEach { r ->
                if (r != s) {
                    createMessage(
                        s.id,
                        r.id,
                        getRndMessage(),
                        List<String>((0..images.size).random()) { images.random() }.toSet().toList()
                    )
                    createMessage(
                        r.id,
                        s.id,
                        getRndMessage(),
                        List<String>((0..images.size).random()) { images.random() }.toSet().toList()
                    )
                }
            }
        }


        /*for (i in 0..99) {
            createMessage(
                users.map { it.id }.random(),
                users.map { it.id }.random(),
                getRndMessage(),
                List<String>((0..images.size).random()) { images.random() }
            )
        }*/
    }

    suspend fun initUsers() {

        var users = List<User>(NUMBER_OF_USERS) {
            var rndPhone = getRndFhone()
            User(
                id = 0,
                phone = rndPhone,
                password = "strong password".hashCode(),
                name = "User # ${rndPhone.substring(8)}",
                avatar = avatars[(0..3).random()]
            )
        }
        userDao.insertUsers(users)
    }

    fun getRndFhone(): String {
        return List(10) { (0..9).random() }.joinToString("", "+7")

    }

    fun getRndMessage(): String {
        return "${striveTo.random()} ${result.random()} ${timeOccasion.random()}"
    }

    suspend fun createMessage(
        sender: Long,
        receiver: Long,
        body: String,
        attachments: List<String> = emptyList()
    ) {
        Database.instance.withTransaction {

            var msg = Messages(
                id = 0,
                sender = sender,
                message_status = MessageStatuses.IS_SENT,
                created_at = Date(System.currentTimeMillis()),
                body = body
            )
            messagesDao.insertMessage(msg)

            var maxMsgId = messagesDao.geMaxId()

            var um = UsersMessages(
                //id = 0,
                message = maxMsgId,
                receiver = receiver
            )
            messagesDao.insertUsersMessages(um)

            //error("Что - то пошло не так !!!")

            var atchList = emptyList<Attachments>()
            attachments.forEach {
                atchList +=
                    Attachments(
                        id = 0,
                        message = maxMsgId,
                        content = it
                    )
            }
            messagesDao.insertAttachments(atchList)
        }
    }

    suspend fun deleteMessageById(id: Long) {
        messagesDao.deleteMessageById(id)
    }


    companion object {
        private const val FIRST_START_SHARED_PREFS = "first_start_info_shared_prefs"
        private const val FIRST_START_FLAG = "first_start_flag"
        private const val NUMBER_OF_USERS = 2
        private const val NUMBER_OF_MESSAGES = 2
    }

}