package com.carryme.dto.requests

import com.carryme.entities.Ferry
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

class FerrySeatRequestDto {
    var id: Long? = null

    @JsonProperty("deck_number")
    var deckNumber: Int? = null

    @JsonProperty("seat_code")
    var seatCode: Int? = null

    var tags: String? = null

    @JsonProperty("is_diffable")
    var isDiffable: Boolean = false

    @JsonProperty("seat_row")
    var seatRow: String? = null

    @JsonProperty("ferry_id")
    var ferryId: Long? = null
}