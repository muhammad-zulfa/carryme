package com.carryme.dto.response

import com.carryme.entities.Ferry
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

class FerryConfigurationDataResponse {
    var seats: List<FerrySeat>? = null
    var detail: FerryDetail? = null

    class FerrySeat{
        @JsonProperty("deck_number")
        var deckNumber: Int? = null
        @JsonProperty("seat_code")
        var seatCode: Int? = null
        var tags: String? = null
        @JsonProperty("is_diffable")
        var isDiffable: Boolean = false
        @JsonProperty("seat_row")
        var seatRow: String? = null
    }
    class FerryDetail{
        @JsonProperty("machine_total")
        var machineTotal: Int? = null
        @JsonProperty("machine_type")
        var machineType: String? = null
        @JsonProperty("machine_brand")
        var machineBrand: String? = null
        @JsonProperty("power")
        var power: String? = null
        @JsonProperty("torque")
        var torque: String? = null
    }
}