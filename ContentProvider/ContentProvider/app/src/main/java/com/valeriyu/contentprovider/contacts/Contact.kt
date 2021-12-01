package com.valeriyu.contentprovider.contacts

data class Contact(
    val id: Long,
    val name: String,
    val famile_name: String = ""
)