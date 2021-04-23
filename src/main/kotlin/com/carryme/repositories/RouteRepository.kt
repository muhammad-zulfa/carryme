package com.carryme.repositories

import com.carryme.entities.Routes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface RouteRepository: CrudRepository<Routes,Long> {
    fun findAllByOriginId(origin: Long) : List<Routes>
    fun findAllByDestinationId(destination: Long): List<Routes>
    fun findAllByOperationRoutesIdAndNameLike(operationRoutes:Long, search: String, pgbl: Pageable): Page<Routes>
    fun findAllByOriginIdAndDestinationId(origin: Long, destination: Long): List<Routes>?
    fun findAllByOperationRoutesId(id: Long): List<Routes>
    fun findAllByOperationRoutesIdAndOriginId(id: Long,origin: Long): List<Routes>
    fun findFirstByOperationRoutesIdAndOriginIdAndDestinationId(id: Long, origin: Long, destination: Long) : Routes

}