package com.carryme.dto.requests

import com.carryme.entities.Ferry
import com.carryme.entities.Routes
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

data class OperationRequestDto(
    var id: Long? = null,
    var ferry: Long? = null,
    var departure: String? = null,
    var route: Long? = null
)