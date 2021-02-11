package com.carryme.dto.requests

import com.carryme.entities.Ferry
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

class FerryDetailRequestDto {
    var id: Long? = null

    @JsonProperty("machine_total")
    var machineTotal: Int? = null

    @JsonProperty("machine_type")
    var machineType: String? = null

    @JsonProperty("machine_brand")
    var machineBrand: String? = null

    @JsonProperty("power")
    var power: String? = null

    @JsonProperty("torque")
    var torque: String? = null

    @JsonProperty("ferry_id")
    var ferryId: Long? = null
}