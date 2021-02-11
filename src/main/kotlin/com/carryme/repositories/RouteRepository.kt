package com.carryme.repositories

import com.carryme.entities.Routes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface RouteRepository: CrudRepository<Routes,Long> {
    fun findAllByOriginId(origin: Long) : List<Routes>
    fun findAllByDestinationId(destination: Long): List<Routes>
    fun findAllByNameLike(search: String, pgbl: Pageable): Page<Routes>

}