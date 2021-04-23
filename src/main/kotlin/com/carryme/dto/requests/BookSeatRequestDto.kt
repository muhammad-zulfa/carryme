package com.carryme.dto.requests

import com.fasterxml.jackson.annotation.JsonProperty

class BookSeatRequestDto {
    @JsonProperty("route_id")
    var routeId: Long? = null

    @JsonProperty("ferry_id")
    var ferryId: Long? = null

    @JsonProperty("operation_id")
    var operationId: Long? = null

    var username: String = ""
    var phone: String = ""
    var fullname: String = ""
    var pax: List<UserData> = listOf()
}