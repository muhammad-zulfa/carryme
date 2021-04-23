package com.carryme.services

import com.carryme.dto.requests.RouteRequestDto
import com.carryme.entities.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IRouteService: IBaseServices<Routes,Long> {
    fun findAllByName(routeOperation: Long, search: String, pgbl: Pageable): Page<Routes>
    fun submit(form: RouteRequestDto): Routes?
    fun deleteAll(id: List<Long>)
    fun findAllByOperation(operation: Long): List<Routes>
}