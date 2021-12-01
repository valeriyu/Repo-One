package com.valeriyu.notifications.app

import com.squareup.moshi.Json

object UserContract {
    const val TABLE_NAME = "users"

    object Columns {
        const val ID = "id"
        const val USER_ID = "userId"
        const val USER_NAME = "userName"
    }
}