package com.valeriyu.a09_intents

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast


object CommonFunctions {

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


        //am = cont.getSystemService(cont.ACTIVITY_SERVICE) as ActivityManager
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
    }


    fun toast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}