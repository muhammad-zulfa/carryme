package com.carryme.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

class BaseResponse(
        val serialVersionUID: Long = 1L,
        val data: Any? = null,
        val status: Status? = null,
        val meta: Any? = null
){

    class Meta(
            private val page: Int = 0,
            private val limit: Int = 0,
            @JsonProperty("total_pages")
            private val totalPages: Int = 0,
            @JsonProperty("total_elements")
            private val totalElements: Long = 0
    )
}