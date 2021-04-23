package com.carryme.services

import com.carryme.dto.requests.RouteRequestDto
import com.carryme.entities.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IOperationRouteService: IBaseServices<OperationRoutes,Long> {
    fun findAllByName(search: String, pgbl: Pageable): Page<OperationRoutes>
    fun submit(form: RouteRequestDto): OperationRoutes
    fun deleteAll(id: List<Long>)
}