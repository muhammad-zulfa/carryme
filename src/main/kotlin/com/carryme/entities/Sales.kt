package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "sales")
class Sales(val id: Long = 0): BaseEntity(id) {

    @Column(name = "total_price")
    @JsonProperty("total_price")
    var totalPrice: Int? = null

    @Column(name = "payment_method")
    @JsonProperty("payment_method")
    var paymentMethod: String? = null

    @Column(name = "payment_expired")
    @JsonProperty("payment_expired")
    @Temporal(TemporalType.TIMESTAMP)
    var paymentExpired: Date? = null

    @Column(name = "payment_proof", nullable = true)
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty("payment_proof")
    var paymentProof: String? = null

    @Column(name = "status")
    var status: String? = null


}