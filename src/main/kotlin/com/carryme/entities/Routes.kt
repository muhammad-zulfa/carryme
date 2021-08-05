package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "routes")
class Routes(val id: Long = 0): BaseEntity(id) {
    @Column(name = "name")
    var name: String? = null

    @ManyToOne
    @JoinColumn(name = "origin")
    var origin: Docks? = null

    @ManyToOne
    @JoinColumn(name = "operation_routes_id")
    var operationRoutes: OperationRoutes? = null

    @ManyToOne
    @JoinColumn(name = "destination")
    var destination: Docks? = null

    @Column(name = "eta")
    var eta: Int? = null

    @Column(name = "price")
    var price: Int? = null

    @Column(name = "orders")
    var orders: Int? = null

    @Column(name = "retribution_fee")
    @JsonProperty("retribution_fee")
    var retributionFee: Int? = null

    @Column(name = "assurance_fee")
    @JsonProperty("assurance_fee")
    var assuranceFee: Int? = null

}