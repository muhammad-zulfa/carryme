package com.carryme.dto.response

import com.carryme.entities.Role
import com.fasterxml.jackson.annotation.JsonProperty

class UserResponseDto {
    var id: Long? = null

    @JsonProperty("fullname")
    var fullname: String? = null

    @JsonProperty("nik")
    var nik: String? = null

    @JsonProperty("phone")
    var phone: String? = null

    @JsonProperty("phone_emerg")
    var phoneEmerg: String? = null

    @JsonProperty("gender")
    var gender: String? = null

    @JsonProperty("pax_type") //adult, child, infant, diffable
    var paxType: String? = null

    var username: String? = null

    @JsonProperty("is_guest")
    var isGuest: Boolean = true

    var active = false

    var roles: List<String>? = null
}