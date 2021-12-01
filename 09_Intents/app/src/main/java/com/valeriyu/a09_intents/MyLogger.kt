package com.valeriyu.a09_intents

import android.util.Log
import android.widget.Toast

object MyLogger {
    fun log(message: String) {
//            val tag = "_a08_activitylifecycle_inside"
/*
        Log.v(tag, " ==> $message")
        Log.d(tag, " ==> $message")
        Log.i(tag, " ==> $message")
        Log.w(tag, " ==> $message")
        Log.e(tag, " ==> $message")
 */
        Log.println(Log.ASSERT, tag, " ==> $message")
    }


}