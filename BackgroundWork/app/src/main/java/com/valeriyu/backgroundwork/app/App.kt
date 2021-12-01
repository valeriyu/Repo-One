package com.valeriyu.backgroundwork.app

import android.app.Application
import com.valeriyu.backgroundwork.notifications.NotificationChannels
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        NotificationChannels.create(this)
    }

}