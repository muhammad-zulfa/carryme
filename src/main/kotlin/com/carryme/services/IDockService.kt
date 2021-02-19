package com.carryme.services

import com.carryme.dto.requests.DockRequestDto
import com.carryme.entities.Docks
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IDockService: IBaseServices<Docks,Long> {
    fun findAllByName(search: String, pgbl: Pageable): Page<Docks>
    fun submit(form: DockRequestDto): Docks
    fun deleteAll(id: List<Long>)
}