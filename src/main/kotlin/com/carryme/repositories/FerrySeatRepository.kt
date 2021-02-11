package com.carryme.repositories

import com.carryme.entities.Ferry
import com.carryme.entities.FerrySeats
import org.springframework.data.repository.CrudRepository
import com.carryme.entities.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FerrySeatRepository: CrudRepository<FerrySeats,Long> {
    fun findAllByDeckNumberAndFerryIdOrderByDeckNumberAscSeatRowAscSeatCodeAsc(deck: Int, ferryId: Long): List<FerrySeats>
    fun findAllByFerryIdOrderByDeckNumberAscSeatRowAscSeatCodeAsc(shipId: Long): List<FerrySeats>
}