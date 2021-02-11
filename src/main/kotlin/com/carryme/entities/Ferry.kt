package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "ferry")
data class Ferry(val id: Long = 0): BaseEntity(id) {
    @Column(name = "name")
    var name: String? = null

    @Column(name = "total_seat")
    @JsonProperty("total_seat")
    var totalSeat: String? = null

    @Column(name = "total_deck")
    @JsonProperty("total_deck")
    var totalDeck: String? = null

    @Column(name = "trip_meter")
    @JsonProperty("trip_meter")
    var tripMeter: String? = null

    @Column(name = "age")
    @JsonProperty("age")
    var age: String? = null

    var availability = false
}