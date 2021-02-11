package com.carryme.services

import com.carryme.dto.requests.FerrySeatGenerateRequestDto
import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.entities.FerrySeats
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IFerrySeatService: IBaseServices<FerrySeats,Long> {
    fun findAllByName(name: String,pageable: Pageable): Page<FerrySeats>
    fun findAllByShipIdAndDeckNumber(shipId: Long, deck: Int?): Map<String, List<List<FerrySeats>>>
    fun submit(shipId: Long,form: FerrySeatRequestDto): FerrySeats
    fun generate(ferryId: Long, form: FerrySeatGenerateRequestDto): List<FerrySeats>
    fun update(id: Long, form: FerrySeatRequestDto): FerrySeats
}