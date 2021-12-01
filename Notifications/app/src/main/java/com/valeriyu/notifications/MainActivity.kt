package com.valeriyu.notifications

import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.core.app.NotificationCompat.getExtras
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.valeriyu.notifications.ui.MenuFragment
import com.valeriyu.notifications.ui.MenuFragmentDirections

class MainActivity : AppCompatActivity(R.layout.activity_main)
