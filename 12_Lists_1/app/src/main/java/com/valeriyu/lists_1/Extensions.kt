package com.valeriyu.lists_1

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.parcel.Parcelize

fun <T : Fragment> T.withArguments(action: Bundle.() -> Unit): T {
    return apply {
        arguments = Bundle().apply(action)
    }
}

fun <T : Activity?> T.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}






