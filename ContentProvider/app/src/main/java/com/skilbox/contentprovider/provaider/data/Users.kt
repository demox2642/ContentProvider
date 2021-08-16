package com.skilbox.contentprovider.provaider.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Users(
    val id: Long,
    @Json(name = "name")
    val name: String,
    val age: Int
)
