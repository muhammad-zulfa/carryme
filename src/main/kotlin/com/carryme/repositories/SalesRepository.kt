package com.carryme.repositories

import com.carryme.entities.FerryDetail
import com.carryme.entities.Sales
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface SalesRepository: CrudRepository<Sales,Long> {
    fun findAllByStatusAndPaymentProofIsNotNull(ferryId: String): List<Sales>
    fun findAllByStatusLikeAndPaymentProofIsNotNull(name: String, pgbl: Pageable): Page<Sales>
    fun findAllByStatusLike(name: String, pgbl: Pageable): Page<Sales>
}