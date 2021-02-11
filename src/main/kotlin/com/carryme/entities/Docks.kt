package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "docks")
class Docks(val id: Long = 0): BaseEntity(id) {
    @Column(name = "name")
    var name: String = ""

    @Column(name = "description")
    @JsonProperty("description")
    var description: String? = null

}