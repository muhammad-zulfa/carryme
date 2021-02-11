package com.carryme.entities

import javax.persistence.*


@Entity
@Table(name = "roles")
data class Role(val id: Long = 0): BaseEntity(id) {
    var name: String? = null

    @ManyToMany(mappedBy = "roles")
    var users: Collection<User>? = null

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "role_privileges",
            joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privileges: Collection<Privileges> = arrayListOf()
}