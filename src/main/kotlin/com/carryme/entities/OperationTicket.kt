package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "operation_ticket")
class OperationTicket(val id: Long = 0): BaseEntity(id) {
    @ManyToOne
    @JoinColumn(name = "ferry_id")
    var ferry: Ferry? = null

    @ManyToOne
    @JoinColumn(name = "seat_id")
    var ferrySeats: FerrySeats? = null

    @ManyToOne
    @JoinColumn(name = "operation_id")
    var operation: Operation? = null

    @Column(name = "booked")
    @JsonProperty("booked",defaultValue = "false")
    var booked: Boolean = false

    @Column(name = "booked_at", nullable = true)
    @JsonProperty("booked_at")
    var bookedAt: Date? = null

    @Column(name = "departure")
    @Temporal(TemporalType.TIMESTAMP)
    var departure: Date? = null

    @ManyToOne
    @JoinColumn(name = "route_id")
    var routes: Routes? = null

    @Column(name = "price")
    @JsonProperty("price")
    var price: Int? = null
}