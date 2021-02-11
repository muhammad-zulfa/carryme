package com.carryme.services.impl

import com.carryme.dto.requests.FerryDetailRequestDto
import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.entities.FerryDetail
import com.carryme.entities.FerrySeats
import com.carryme.repositories.FerryDetailRepository
import com.carryme.repositories.FerryRepository
import com.carryme.repositories.FerrySeatRepository
import com.carryme.services.IFerryDetailService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FerryDetailService: IFerryDetailService{
    @Autowired
    private lateinit var repository: FerryDetailRepository

    @Autowired
    private lateinit var ferryRepository: FerryRepository

    override fun findByShipId(shipId: Long): FerryDetail? {
        return repository.findByFerryId(shipId)
    }

    override fun submit(form: FerryDetailRequestDto): FerryDetail? {
        val ferry = when(form.id) {
            null -> FerryDetail()
            else -> repository.findById(form.id!!).get()
        }
        BeanUtils.copyProperties(form,ferry)
        ferry.ferry = ferryRepository.findById(form.ferryId!!).get()
        return repository.save(ferry)
    }

    override fun findAll(pageable: Pageable?): Page<FerryDetail>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<FerryDetail>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): FerryDetail {
        return repository.findById(id).get()
    }

    override fun save(entity: FerryDetail): FerryDetail {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}