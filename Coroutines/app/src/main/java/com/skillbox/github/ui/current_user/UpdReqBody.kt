package com.skillbox.github.ui.current_user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdReqBody (
        @Json(name = "name")
        var name:String? = "" ,
        @Json(name = "company")
        var company:String? = "",
        @Json(name = "location")
        var location:String? =  ""
)