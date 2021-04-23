package com.carryme.dto.response

import com.carryme.entities.TicketSales
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Column
import javax.persistence.Temporal
import javax.persistence.TemporalType

class SalesResponseDto {
    var id: Long? = null

    @JsonProperty("total_price")
    var totalPrice: Int? = null

    @JsonProperty("payment_method")
    var paymentMethod: String? = null

    @JsonProperty("payment_expired")
    var paymentExpired: Date? = null

    @JsonProperty("payment_proof")
    var paymentProof: String? = null

    var status: String? = null

    @JsonProperty("ticket_sales")
    var ticketSales: List<TicketSales> = listOf()
}