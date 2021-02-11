package com.carryme.services.impl

import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.entities.FerrySeats
import com.carryme.entities.TicketSales
import com.carryme.repositories.FerrySeatRepository
import com.carryme.services.IFerryDetailService
import com.carryme.services.ITicketSalesService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TicketSalesService: ITicketSalesService{
    override fun findAll(pageable: Pageable?): Page<TicketSales>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<TicketSales>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): TicketSales {
        TODO("Not yet implemented")
    }

    override fun save(entity: TicketSales): TicketSales {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}