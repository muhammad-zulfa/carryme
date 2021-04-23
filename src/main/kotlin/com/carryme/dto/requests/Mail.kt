package com.carryme.dto.requests

data class Mail(
    val subject: String,
    val to: String,
    val from: String,
    val template: String,
    val props: MutableMap<String, Any>?
)