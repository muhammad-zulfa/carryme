package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "operation")
class Operation(val id: Long = 0): BaseEntity(id) {
    @ManyToOne
    @JoinColumn(name = "ferry_id")
    var ferry: Ferry? = null


    @Column(name = "departure")
    @Temporal(TemporalType.TIMESTAMP)
    var departure: Date? = null

    @ManyToOne
    @JoinColumn(name = "route_id")
    var routes: Routes? = null

}