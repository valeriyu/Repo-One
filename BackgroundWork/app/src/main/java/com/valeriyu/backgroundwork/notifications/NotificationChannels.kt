package com.valeriyu.backgroundwork.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

object NotificationChannels {

    val DOWNLOAD_CHANNEL_ID = "download_channel"
    val RADIO_NOTIFICATION_ID = 707
    val NOTIFICATION_RADIO_CHANNEL_ID = "notification_radio_channel"

    fun create(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDownloadChannel(context)
            creadteRadioNotificationChannel(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun creadteRadioNotificationChannel(context: Context) {
        val priority = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            NOTIFICATION_RADIO_CHANNEL_ID,
            "Radio",
            priority
        )
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDownloadChannel(context: Context) {
        val name = "Download"
        val priority = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(DOWNLOAD_CHANNEL_ID, name, priority)
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }


}