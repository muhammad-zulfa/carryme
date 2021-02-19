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
    @Autowired
    private lateinit var repository: DockRepository
    override fun findAllByName(search: String, pgbl: Pageable): Page<Docks> {
        return repository.findAllByNameLike(search,pgbl)
    }

    override fun submit(form: DockRequestDto): Docks {
        val dock = when(form.id){
            null -> Docks()
            else -> repository.findById(form.id!!).get()
        }
        BeanUtils.copyProperties(form,dock)
        return repository.save(dock)
    }

    override fun deleteAll(id: List<Long>) {
        repository.deleteAll(repository.findAllById(id))
    }

    override fun findAll(pageable: Pageable?): Page<Docks>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<Docks>? {
        return repository.findAll()
    }

    override fun findById(id: Long): Docks {
        return repository.findById(id).get()
    }

    override fun save(entity: Docks): Docks {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}