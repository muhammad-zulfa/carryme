package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.configurationprocessor.json.JSONArray
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
@Table(name = "ferry_seats")
data class FerrySeats(val id: Long = 0): BaseEntity(id) {
    @Column(name = "deck_number")
    @JsonProperty("deck_number")
    var deckNumber: Int? = null

    @Column(name = "seat_code")
    @JsonProperty("seat_code")
    var seatCode: Int? = null

    @Column(name = "tags",columnDefinition = "json")
    var tags: String? = null

    @Column(name = "is_diffable")
    @JsonProperty("is_diffable")
    var isDiffable: Boolean = false

    @Column(name = "seat_row")
    @JsonProperty("seat_row")
    var seatRow: String? = null

    @Column(name = "is_enable")
    @JsonProperty("is_enable")
    var isEnable: Boolean? = true

    @ManyToOne
    @JoinColumn(name = "ferry_id")
    var ferry: Ferry? = null

}