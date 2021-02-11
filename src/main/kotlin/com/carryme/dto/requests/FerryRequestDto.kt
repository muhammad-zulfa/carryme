package com.carryme.dto.requests

import com.fasterxml.jackson.annotation.JsonProperty

class FerryRequestDto {
    var id: Long? = null

    var name: String? = null


    @JsonProperty("total_seat")
    var totalSeat: String? = null

    @JsonProperty("total_deck")
    var totalDeck: String? = null

    @JsonProperty("trip_meter")
    var tripMeter: String? = null

    @JsonProperty("age")
    var age: String? = null

    var availability = false
}