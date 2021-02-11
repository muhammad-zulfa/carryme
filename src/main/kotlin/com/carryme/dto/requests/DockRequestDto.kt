package com.carryme.dto.requests

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column

class DockRequestDto {
    var id: Long? = null

    var name: String = ""

    var description: String? = null
}