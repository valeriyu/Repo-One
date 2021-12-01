package com.valeriyu.viewmodelandnavigation

sealed class Tools {
    data class Tool(
        // val id: Long,
        val name: String,
        var pictLink: String,
        val description: String = ""
    ) : Tools()

    data class ProflTool(
        // val id: Long,
        val name: String,
        var pictLink: String,
        val description: String = "",
        val weight: Double = 0.0
    ) : Tools()

    data class Separator(
        val height: Int = 64
    ) : Tools()
}




