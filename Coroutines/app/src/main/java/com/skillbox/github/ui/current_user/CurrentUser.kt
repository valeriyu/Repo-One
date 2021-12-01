package com.skillbox.github.ui.current_user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentUser(
    @Json(name = "login")
    val username: String,
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    var name:String? = "" ,
    @Json(name = "company")
    var company:String? = "",
    @Json(name = "location")
    var location:String? =  "",
    @Json(name = "avatar_url")
    var avatar_url:String? = ""
)




