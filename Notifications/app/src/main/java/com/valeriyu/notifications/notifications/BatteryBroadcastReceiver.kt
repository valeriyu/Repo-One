package com.valeriyu.notifications.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.valeriyu.notifications.R

class BatteryBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        val isLow = intent?.action == Intent.ACTION_BATTERY_LOW
        if(isLow) {
            showLowBatteryNotification(context)
        } else {
            hideLowBatteryNotification(context)
        }
    }

    private fun showLowBatteryNotification(context: Context) {
        val notification = NotificationCompat.Builder(context,
            NotificationChannels.HI_PRIORITY_CHANNEL_ID
        )
            .setContentTitle("Battery is low")
            .setSmallIcon(R.drawable.ic_notifications)
            .build()

        NotificationManagerCompat.from(context)
            .notify(BATTERY_NOTIFICATION_ID, notification)
    }

    private fun hideLowBatteryNotification(context: Context) {
        NotificationManagerCompat.from(context)
            .cancel(BATTERY_NOTIFICATION_ID)
    }

    companion object {
        private const val BATTERY_NOTIFICATION_ID = 6543
    }

}