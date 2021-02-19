package com.carryme.services.impl

import com.carryme.dto.requests.FerryRequestDto
import com.carryme.entities.Ferry
import com.carryme.repositories.FerryRepository
import com.carryme.services.IFerryService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class FerryService: IFerryService{
    @Autowired
    lateinit var ferryRepository: FerryRepository

    override fun findAllByName(name: String,pageable: Pageable): Page<Ferry> {
        return ferryRepository.findAllByNameLike(name,pageable)
    }

    override fun submit(form: FerryRequestDto): Ferry {
        var ferry = when(form.id){
            null -> Ferry()
            else -> ferryRepository.findById(form.id!!).get()
        }
        BeanUtils.copyProperties(form,ferry)
        return ferryRepository.save(ferry)
    }

    override fun loadConfigurationData(ferryId: Long) {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable?): Page<Ferry>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<Ferry>? {
        return ferryRepository.findAll()
    }

    override fun findById(id: Long): Ferry {
        return ferryRepository.findById(id).get()
    }

    override fun save(entity: Ferry): Ferry {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}