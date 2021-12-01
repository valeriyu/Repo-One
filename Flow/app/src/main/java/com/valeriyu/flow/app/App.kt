package com.valeriyu.flow.app

import android.app.Application
import android.os.StrictMode
import com.valeriyu.flow.Database
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //Позволяет совершать запросы в интернет на главном потоке
      /*  StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitNetwork()
                .build()
        )*/

        Timber.plant(Timber.DebugTree())
        Database.init(this)
    }
}