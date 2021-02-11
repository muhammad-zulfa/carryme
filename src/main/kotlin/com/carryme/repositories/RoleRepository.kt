package com.carryme.repositories

import org.springframework.data.repository.CrudRepository
import com.carryme.entities.Role
import com.carryme.entities.User

interface RoleRepository: CrudRepository<Role,Long> {
    fun findByName(name: String): Role?
}