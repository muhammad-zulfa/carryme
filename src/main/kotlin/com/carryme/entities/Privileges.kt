package com.carryme.entities

import com.carryme.entities.Role
import javax.persistence.*


@Entity
@Table(name = "privileges")
data class Privileges(val id: Long = 0): BaseEntity(id) {
    var name: String? = null

    @ManyToMany(mappedBy = "privileges")
    var roles: Collection<Role>? = null
}