package com.valeriyu.roomdao.app

import android.app.Application
import timber.log.Timber

class MessangerApp : Application(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Database.init(this)
    }
}