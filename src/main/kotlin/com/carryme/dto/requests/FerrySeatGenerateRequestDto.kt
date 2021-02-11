package com.carryme.dto.requests

import com.fasterxml.jackson.annotation.JsonProperty

class FerrySeatGenerateRequestDto {
    @JsonProperty("total_seat")
    val totalSeat: Int = 0

    @JsonProperty("deck_number")
    val deckNumber: Int = 0
}