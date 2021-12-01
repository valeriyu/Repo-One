package com.valeriyu.notifications

import android.app.Application
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import com.valeriyu.notifications.app.Database
import com.valeriyu.notifications.notifications.NotificationChannels
import timber.log.Timber
import com.valeriyu.notifications.*


@RequiresApi(Build.VERSION_CODES.O)
class NotificationApp : Application() {

    override fun onCreate() {
        super.onCreate()
        //Позволяет совершать запросы в интернет на главном потоке
      /*  StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitNetwork()
                .build()
        )*/

        Timber.plant(Timber.DebugTree())
        NotificationChannels.create(this)
        Database.init(this)
    }
}