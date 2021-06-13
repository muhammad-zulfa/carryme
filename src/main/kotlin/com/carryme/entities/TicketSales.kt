package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tiket_sales")
class TicketSales(val id: Long = 0): BaseEntity(id) {

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    var ticket: OperationTicket? = null

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null

    @Column(name = "notes", nullable = true)
    var notes: String? = null

    @ManyToOne
    @JoinColumn(name = "sales_id")
    var sales: Sales? = null

    @Column(name = "checked_in")
    var checkedIn: Boolean? = false
}