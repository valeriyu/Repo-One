package com.valeriyu.scopedstorage

import android.net.Uri

data class Movie(
    var id: Long,
    val uri: Uri,
    val name: String,
    val size: Long,
    var favorite: Boolean = false,
    var trashed: Boolean = false,
    val path: String = ""
)