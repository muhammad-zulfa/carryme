package com.carryme.repositories

import org.springframework.data.repository.CrudRepository
import com.carryme.entities.User

interface UserRepository: CrudRepository<User,Long> {
    fun findByUsername(username: String): User?
}