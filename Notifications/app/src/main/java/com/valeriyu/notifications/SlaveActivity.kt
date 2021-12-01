package com.valeriyu.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.valeriyu.notifications.ui.ListFragment

class SlaveActivity : AppCompatActivity(R.layout.activity_slave) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.listConainer, ListFragment(), "LIST")
                //.addToBackStack(null)
                .commit()
        }
    }
}