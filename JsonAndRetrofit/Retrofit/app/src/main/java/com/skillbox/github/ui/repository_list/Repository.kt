package com.skillbox.github.ui.repository_list


import com.skillbox.github.ui.current_user.CurrentUser
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Repository(
        val id: String,
        var name: String,
        var full_name: String
        ,
        var owner: CurrentUser,
        var isStarred:Boolean = false
)