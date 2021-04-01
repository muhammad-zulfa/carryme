package com.carryme.repositories

import com.carryme.entities.Operation
import org.springframework.data.domain.Page
import org.springframework.data.repository.CrudRepository
import org.springframework.data.domain.Pageable
import java.util.*

interface OperationRepository: CrudRepository<Operation,Long> {
    fun findAllByRoutesIdAndDepartureGreaterThanEqual(routes: Long,departure: Date,pageable: Pageable): Page<Operation>
    fun findAllByFerryNameLike(name: String, pageable: Pageable): Page<Operation>
}