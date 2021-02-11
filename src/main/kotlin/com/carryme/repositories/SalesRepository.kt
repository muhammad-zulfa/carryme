package com.carryme.repositories

import com.carryme.entities.FerryDetail
import com.carryme.entities.Sales
import org.springframework.data.repository.CrudRepository

interface SalesRepository: CrudRepository<Sales,Long> {
    fun findAllByStatusAndPaymentProofIsNotNull(ferryId: String): List<Sales>
}