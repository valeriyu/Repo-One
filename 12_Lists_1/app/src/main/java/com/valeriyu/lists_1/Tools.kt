package com.valeriyu.lists_1

import android.annotation.SuppressLint
import androidx.annotation.StringRes

sealed class Tools {
    data class Tool(
        val name: String,
        var pictLink: String,
        val description: String = ""
    ) : Tools()

    data class ProflTool(
        val name: String,
        var pictLink: String,
        val description: String = "",
        val weight: Double = 0.0
    ) : Tools()

}
