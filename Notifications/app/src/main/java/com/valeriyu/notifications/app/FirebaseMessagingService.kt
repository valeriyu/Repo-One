package com.valeriyu.notifications.app

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.moshi.Moshi
import com.valeriyu.notifications.*
import com.valeriyu.notifications.models.Promotions
import com.valeriyu.notifications.notifications.ApiClient
import com.valeriyu.notifications.notifications.NotificationChannels
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.random.Random

class FirebaseMessagingService : FirebaseMessagingService() {

    private var notificationsRepository = NotificationsRepository(this)
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var message = RemoteInput.getResultsFromIntent(intent)?.getCharSequence(
                KEY_TEXT_REPLY
            )

            var notoficationId = 0
            var arguments = intent?.getExtras()
            if (arguments != null) {
                notoficationId = arguments.getInt(EXTRA_NOTIFICATION_ID)
            }

            Timber.d(message.toString())
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            val repliedNotification = context?.let {
                NotificationCompat.Builder(
                    it,
                    NotificationChannels.HI_PRIORITY_CHANNEL_ID
                )
                    .setSmallIcon(R.drawable.ic_message)
                    .setContentText("Ответ отправлен")
                    .setAutoCancel(true)
                    .build()
            }

            if (repliedNotification != null) {
                NotificationManagerCompat.from(context)
                    .notify(notoficationId, repliedNotification)
            }
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        ApiClient.Token.value = p0
    }

    override fun onCreate() {
        super.onCreate()
        val intFilt = IntentFilter(REPLAY_ACTION)
        registerReceiver(broadcastReceiver, intFilt)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
        coroutineScope.cancel()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val type = message.data["type"]
        val dataJson = message.data["data"]
        when (type) {
            "promotions" -> showPromotionsMessageNotification(dataJson)
            "chat" -> showChatMessageNotification(dataJson)
            else -> return
        }
    }

    private fun showPromotionsMessageNotification(dataJson: String?) {
        Timber.d(dataJson)

        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Promotions::class.java).nonNull()
        val promotions: Promotions

        try {
            promotions = adapter.fromJson(dataJson)!!
        } catch (e: Exception) {
            return
        }

        coroutineScope.launch {
            notificationsRepository.insertPromotions(listOf(promotions))
        }


        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 123, intent, 0)
        var largeIcon = promotions.imageUrl?.let { loadBitmapWithGlide(it) } ?: null

        val notification = NotificationCompat.Builder(
            this,
            NotificationChannels.LO_PRIORITY_CHANNEL_ID
        )
            .setContentTitle("${promotions.title}")
            //.setContentText("${promotions.description}")
            .setStyle(NotificationCompat.BigTextStyle().bigText("${promotions.description}"))

            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_baseline_add_shopping_cart_24)
            .setLargeIcon(largeIcon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(this)
            .notify(1111, notification)
    }

    private fun showChatMessageNotification(dataJson: String?) {
        Timber.d(dataJson)

        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Message::class.java).nonNull()
        val msg: Message

        try {
            msg = adapter.fromJson(dataJson)!!
        } catch (e: Exception) {
            return
        }

        msg.created_at = System.currentTimeMillis()
        coroutineScope.launch {
            notificationsRepository.insertMessage(msg)
        }

        val intent = Intent(this, SlaveActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 123, intent, 0)
        var notoficationId = Random.nextInt()

        val replayIntent = Intent().apply {
            action = REPLAY_ACTION
            putExtra(EXTRA_NOTIFICATION_ID, notoficationId)
        }

        var replyLabel: String = "Введите текст ответа"
        var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }

        var replyPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                applicationContext,
                123,
                replayIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        var action: NotificationCompat.Action =
            NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_replay_24,
                "Ответить", replyPendingIntent
            )
                .addRemoteInput(remoteInput)
                .build()

        val notification = NotificationCompat.Builder(
            this,
            NotificationChannels.HI_PRIORITY_CHANNEL_ID
        )
            .setContentTitle("Сообщение от: ${msg.user?.userName}")
            .setContentText("${msg.text}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(action)
            .build()

        NotificationManagerCompat.from(this)
            .notify(notoficationId, notification)

        Timber.d(getMessageText(intent).toString())
    }

    private fun getMessageText(intent: Intent): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_TEXT_REPLY)
    }

    private fun loadBitmapWithGlide(url: String): Bitmap {
        val bitmap = Glide.with(this)
            .asBitmap()
            .load(url)
            .submit()
            .get()
        return bitmap
    }

    private companion object {
        const val KEY_TEXT_REPLY = "key_text_reply"
        const val REPLAY_ACTION = "com.valeriyu.notifications.reply_to_action"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }
}