package com.carryme.services

import com.carryme.dto.requests.FerryRequestDto
import com.carryme.entities.Ferry
import com.carryme.entities.FerrySeats
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IFerryService: IBaseServices<Ferry,Long> {
    fun findAllByName(name: String,pageable: Pageable): Page<Ferry>
    fun submit(form: FerryRequestDto): Ferry
    fun loadConfigurationData(ferryId: Long)
}