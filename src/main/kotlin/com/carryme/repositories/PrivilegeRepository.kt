package com.carryme.repositories

import org.springframework.data.repository.CrudRepository
import com.carryme.entities.Privileges
import com.carryme.entities.Role
import com.carryme.entities.User

interface PrivilegeRepository: CrudRepository<Privileges,Long> {
    fun findByName(name: String): Privileges?
}