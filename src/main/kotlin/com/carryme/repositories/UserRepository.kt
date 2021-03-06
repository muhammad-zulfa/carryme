package com.carryme.repositories

import org.springframework.data.repository.CrudRepository
import com.carryme.entities.User
import org.springframework.data.jpa.repository.Query
import java.math.BigInteger

interface UserRepository: CrudRepository<User,Long> {
    fun findByUsername(username: String): User?

    @Query(
        nativeQuery = true,
        value = "select DATE(created_at), count(created_at) from users group by DATE(created_at) having DATE(created_at) > (current_date - interval '-7 days')"
    )
    fun findNewDataPerDay(): List<Map<String,BigInteger>>
    fun findAllByGuestUserId(id: Long): List<User>
}