package com.valeriyu.a09_intents

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_deeplink.*
import kotlinx.android.synthetic.main.activity_deeplink.onInfoClick
import kotlinx.android.synthetic.main.activity_start.*

// android:launchMode="singleInstance"
// android:launchMode="singleTask"

class DeeplinkActivity : AppCompatActivity() {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val LOG_TAG = "__a09_intents_inside"
        Log.d(LOG_TAG, "!------------------!")
        Log.d(LOG_TAG, "Вызван onNewIntent !!!")
        Log.d(LOG_TAG, "!------------------!")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deeplink)
        // am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        setSupportActionBar(toolbar)

        intent.data?.let {
            textViewUri.text = it.toString()
        }

        buttonDeepLink.setOnClickListener {

            CommonFunctions.openWebPage(this, packageManager, "https://yandex.ru/pogoda/moscow?via=moc")
            //openWebPage(this, packageManager, "https://yandex.ru/pogoda/moscow?via=moc")
        }

        onInfoClick.setOnClickListener{
            var am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            CommonFunctions.onInfoClick(am)
            //onInfoClick()
        }

        onInfoClick.isVisible = INFO_ENABLE
    }


  /*  fun onInfoClick() {
        var am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //CommonFunctions.onInfoClick(am)
        onInfoClick(am)

    }

    fun openWebPage(context: Context, packageManager: PackageManager, url: String) {
        val webpage: Uri = Uri.parse(url)
        val webPageIntent = Intent(Intent.ACTION_VIEW, webpage)
        if (webPageIntent.resolveActivity(packageManager) != null) {
            context.startActivity(webPageIntent)
        }
    }

  fun onInfoClick(am: ActivityManager? = null) {

        val LOG_TAG = "__a09_intents_inside"
        var list: List<ActivityManager.RunningTaskInfo>? = null

        //var am: ActivityManager? = null
        //am = getSystemService(ACTIVITY_SERVICE) as ActivityManager

        list = am!!.getRunningTasks(10)
        for (task in (list as MutableList<ActivityManager.RunningTaskInfo>?)!!) {
            if (task.baseActivity!!.flattenToShortString().startsWith("com.valeriyu.a09_intents")) {
                Log.d(LOG_TAG, "------------------")
                Log.d(LOG_TAG, "Count: " + task.numActivities)
                Log.d(LOG_TAG, "Root: " + task.baseActivity!!.flattenToShortString())
                Log.d(LOG_TAG, "Top: " + task.topActivity!!.flattenToShortString())
                Log.d(LOG_TAG, "taskId: " + task.taskId!!.toString())
            }
        }
    }*/
}
