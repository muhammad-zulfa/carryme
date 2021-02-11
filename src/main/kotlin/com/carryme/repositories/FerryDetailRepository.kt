package com.carryme.repositories

import com.carryme.entities.FerryDetail
import org.springframework.data.repository.CrudRepository

interface FerryDetailRepository: CrudRepository<FerryDetail,Long> {
    fun findByFerryId(ferryId: Long): FerryDetail?
}