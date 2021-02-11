package com.carryme.repositories

import com.carryme.entities.TicketSales
import org.springframework.data.repository.CrudRepository

interface TicketSalesRepository: CrudRepository<TicketSales,Long> {
    fun findAllBySalesId(salesId: Long): List<TicketSales>
}