package com.carryme.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(val id: Long = 0): BaseEntity(id) {
    @Column(name = "fullname")
    var fullname: String? = null

    @Column(name = "nik", nullable = true)
    var nik: String? = null

    @Column(name = "phone",nullable = true)
    var phone: String? = null

    @Column(name = "phone_emerg")
    @JsonProperty("phone_emerg")
    var phoneEmerg: String? = null

    @Column(name = "gender")
    var gender: String? = null

    @Column(name = "pax_type") //adult, child, infant, diffable
    @JsonProperty("pax_type")
    var paxType: String? = null

    @Column(nullable = true)
    var username: String? = null

    @JsonIgnore
    @Column(nullable = true)
    var password: String? = null

    @Column(name = "is_guest")
    @JsonProperty("is_guest")
    var isGuest: Boolean = true

    var active = false

    @JsonIgnore
    @Column(name = "token_expired")
    @JsonProperty("token_expired")
    var tokenExpired = false

    @JsonIgnore
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles: Collection<Role>? = null

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(
        name = "guest_user",
    )
    var guestUser: User? = null
}