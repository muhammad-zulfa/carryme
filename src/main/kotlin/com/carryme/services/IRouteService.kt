package com.carryme.services

import com.carryme.dto.requests.FerryRequestDto
import com.carryme.dto.requests.RouteRequestDto
import com.carryme.entities.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IRouteService: IBaseServices<Routes,Long> {
    fun findAllByName(search: String, pgbl: Pageable): Page<Routes>
    fun submit(form: RouteRequestDto): Routes?
}