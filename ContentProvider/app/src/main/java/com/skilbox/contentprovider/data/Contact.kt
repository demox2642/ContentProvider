package com.skilbox.contentprovider.data

data class Contact(
    val id: Long,
    val name: String,
    val phones: List<String>,
    val email: String,
    val photo: String?
)
