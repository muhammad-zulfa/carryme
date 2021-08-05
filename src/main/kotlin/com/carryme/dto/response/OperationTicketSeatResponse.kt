package com.carryme.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column

class OperationTicketSeatResponse {
    var id: Long? = null

    var deckNumber: Int? = null

    var seatCode: Int? = null

    var tags: String? = null

    var isDiffable: Boolean = false

    var isEnable: Boolean = true

    var seatRow: String? = null

    var status: OperationStatus? = null

    class OperationStatus{
        var id: Long? = null

        var booked: Boolean = false
    }
}