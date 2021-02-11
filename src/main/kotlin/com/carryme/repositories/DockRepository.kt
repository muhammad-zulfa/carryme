package com.carryme.repositories

import com.carryme.entities.Docks
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface DockRepository: CrudRepository<Docks,Long> {
    fun findAllByNameLike(search: String, pgbl: Pageable): Page<Docks>
}