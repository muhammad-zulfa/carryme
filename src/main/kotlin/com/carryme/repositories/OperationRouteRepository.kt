package com.carryme.repositories

import com.carryme.entities.OperationRoutes
import com.carryme.entities.Routes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface OperationRouteRepository: CrudRepository<OperationRoutes,Long> {
    fun findAllByOriginId(origin: Long) : List<OperationRoutes>
    fun findAllByDestinationId(destination: Long): List<OperationRoutes>
    fun findAllByNameLike(search: String, pgbl: Pageable): Page<OperationRoutes>
    fun findByOriginIdAndDestinationId(origin: Long, destination: Long): OperationRoutes?

}