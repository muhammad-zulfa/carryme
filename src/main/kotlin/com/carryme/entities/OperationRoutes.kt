package com.carryme.entities

import javax.persistence.*

@Entity
@Table(name = "operation_routes")
class OperationRoutes(var id: Long = 0): BaseEntity(id) {
    @Column(name = "name")
    var name: String? = null

    @ManyToOne
    @JoinColumn(name = "origin")
    var origin: Docks? = null

    @ManyToOne
    @JoinColumn(name = "destination")
    var destination: Docks? = null

    @Column(name = "eta")
    var eta: Int? = null

    @Column(name = "price")
    var price: Int? = null
}