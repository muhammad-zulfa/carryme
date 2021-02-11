package com.carryme.services.impl

import com.carryme.dto.requests.DockRequestDto
import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.entities.Docks
import com.carryme.entities.FerrySeats
import com.carryme.repositories.DockRepository
import com.carryme.repositories.FerrySeatRepository
import com.carryme.services.IDockService
import com.carryme.services.IFerryDetailService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DockService: IDockService{
    private lateinit var repository: DockRepository
    override fun findAllByName(search: String, pgbl: Pageable): Page<Docks> {
        return repository.findAllByNameLike(search,pgbl)
    }

    override fun submit(form: DockRequestDto): Docks {
        val dock = Docks()
        BeanUtils.copyProperties(form,dock)
        return repository.save(dock)
    }

    override fun findAll(pageable: Pageable?): Page<Docks>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<Docks>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Docks {
        TODO("Not yet implemented")
    }

    override fun save(entity: Docks): Docks {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}