package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "ferry_detail")
data class FerryDetail(val id: Long = 0): BaseEntity(id) {
    @Column(name = "machine_total")
    @JsonProperty("machine_total")
    var machineTotal: Int? = null

    @Column(name = "machine_type")
    @JsonProperty("machine_type")
    var machineType: String? = null

    @Column(name = "machine_brand")
    @JsonProperty("machine_brand")
    var machineBrand: String? = null

    @Column(name = "power")
    @JsonProperty("power")
    var power: String? = null

    @Column(name = "torque")
    @JsonProperty("torque")
    var torque: String? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ferry_id")
    var ferry: Ferry? = null
}