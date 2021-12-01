package com.valeriyu.notifications.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat


@RequiresApi(Build.VERSION_CODES.O)
object NotificationChannels {

    val HI_PRIORITY_CHANNEL_ID = "hi_priority_channel"
    val LO_PRIORITY_CHANNEL_ID = "lo_priority_channel"
    val DOWNLOAD_CHANNEL_ID = "download_channel"
    val SYNCHRO_CHANNEL_ID = "synchro_channel"


    fun create(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createPromotionsChannel(context)
            createChatChannel(context)
            createDownloadChannel(context)
            createSynchronizeChannel(context)
        }
    }

    private fun createSynchronizeChannel(context: Context) {
        val name = "Synchronize"
        val priority = NotificationManager.IMPORTANCE_HIGH
        val ringURI = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context!!.packageName}/raw/silent")
        val channel = NotificationChannel(SYNCHRO_CHANNEL_ID, name, priority).apply {
            setSound(ringURI, audioAttributes)
            enableVibration(false)
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    private fun createDownloadChannel(context: Context) {
        val name = "Download"
        val priority = NotificationManager.IMPORTANCE_HIGH
        val ringURI = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context!!.packageName}/raw/silent")
        val att = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val channel = NotificationChannel(DOWNLOAD_CHANNEL_ID, name, priority)
            .apply {
                setSound(ringURI, audioAttributes)
                enableVibration(false)
            }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    private fun createPromotionsChannel(context: Context) {
        val name = "Promotions"
        val channelDescription = "Messages about new promotions"
        val priority = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(LO_PRIORITY_CHANNEL_ID, name, priority).apply {
            description = channelDescription
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    private fun createChatChannel(context: Context) {
        val name = "Chat"
        val channelDescription = "Chat messages"
        val priority = NotificationManager.IMPORTANCE_HIGH

        val ringURI = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/doink")
        val att = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        val channel = NotificationChannel(HI_PRIORITY_CHANNEL_ID, name, priority).apply {
            description = channelDescription
            //setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
            setSound(ringURI, att)
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 500, 500)
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}