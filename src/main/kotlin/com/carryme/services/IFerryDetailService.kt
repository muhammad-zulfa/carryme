package com.carryme.services

import com.carryme.dto.requests.FerryDetailRequestDto
import com.carryme.dto.requests.FerryRequestDto
import com.carryme.entities.Ferry
import com.carryme.entities.FerryDetail
import com.carryme.entities.FerrySeats
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IFerryDetailService: IBaseServices<FerryDetail,Long> {
    fun findByShipId(shipId: Long): FerryDetail?
    fun submit(form: FerryDetailRequestDto): FerryDetail?
}