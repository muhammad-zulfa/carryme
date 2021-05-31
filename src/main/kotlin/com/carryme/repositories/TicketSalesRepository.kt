package com.carryme.repositories

import com.carryme.entities.TicketSales
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface TicketSalesRepository: CrudRepository<TicketSales,Long> {
    fun findAllBySalesId(salesId: Long): List<TicketSales>

    @Query(
        value = "select ts from TicketSales ts join Sales s on s.id = ts.sales.id " +
                "where s.status = ?1 and s.createdAt between ?2 and ?3"
    )
    fun findReport(s: String, start: Date?, end: Date?): List<TicketSales>
}