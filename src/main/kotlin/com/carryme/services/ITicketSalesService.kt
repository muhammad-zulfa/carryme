package com.carryme.services

import com.carryme.dto.response.DashboardDTO
import com.carryme.entities.TicketSales

interface ITicketSalesService: IBaseServices<TicketSales,Long> {
    fun getDataCount(): DashboardDTO
    fun getReport(dateStart: String, dateEnd: String): String
    fun checkedIn(id: Long): TicketSales
}