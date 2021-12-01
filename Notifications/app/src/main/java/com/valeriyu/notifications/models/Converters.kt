package com.valeriyu.notifications.models

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


class Converters {

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun fromTimestamp(value: Long?): String? {
        return if (value == null) null
        else {
            val sdf = SimpleDateFormat("dd-MM-yy HH:mm:ss")
            sdf.format(value)
        }
    }


}