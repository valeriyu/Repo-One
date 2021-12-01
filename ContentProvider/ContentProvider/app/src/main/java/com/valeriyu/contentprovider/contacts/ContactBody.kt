package com.valeriyu.contentprovider.contacts

data class ContactBody(
    var id: Long,
    var phones: List<String>,
    var email: List<String>
)