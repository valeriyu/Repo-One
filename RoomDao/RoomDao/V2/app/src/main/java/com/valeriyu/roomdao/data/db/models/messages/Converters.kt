package com.valeriyu.roomdao.data.db.models.messages

import androidx.room.TypeConverter
import java.util.*


class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }


    @TypeConverter
    fun convertMessageStatusesToString(status: MessageStatuses): String = status.name
    @TypeConverter
    fun convertStringToMessageStatusses(statusString: String): MessageStatuses =
        MessageStatuses.valueOf(statusString)

}