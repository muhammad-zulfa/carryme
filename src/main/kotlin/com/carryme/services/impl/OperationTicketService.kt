package com.carryme.services.impl

import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.entities.FerrySeats
import com.carryme.entities.OperationTicket
import com.carryme.repositories.FerrySeatRepository
import com.carryme.services.IFerryDetailService
import com.carryme.services.IOperationTicketService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OperationTicketService: IOperationTicketService{
    override fun findAll(pageable: Pageable?): Page<OperationTicket>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<OperationTicket>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): OperationTicket {
        TODO("Not yet implemented")
    }

    override fun save(entity: OperationTicket): OperationTicket {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}