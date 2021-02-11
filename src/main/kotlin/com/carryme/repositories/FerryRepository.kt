package com.carryme.repositories

import com.carryme.entities.Ferry
import org.springframework.data.repository.CrudRepository
import com.carryme.entities.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FerryRepository: CrudRepository<Ferry,Long> {
    fun findAllByNameLike(name: String,pageable: Pageable): Page<Ferry>
}