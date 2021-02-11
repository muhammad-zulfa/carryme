package com.carryme.services.impl

import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.entities.FerrySeats
import com.carryme.entities.Sales
import com.carryme.repositories.FerrySeatRepository
import com.carryme.services.IFerryDetailService
import com.carryme.services.ISalesService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SalesService: ISalesService{
    override fun findAll(pageable: Pageable?): Page<Sales>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<Sales>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Sales {
        TODO("Not yet implemented")
    }

    override fun save(entity: Sales): Sales {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}