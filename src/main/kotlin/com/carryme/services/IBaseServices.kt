package com.carryme.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.io.Serializable

interface IBaseServices<E,I: Serializable> {
    fun findAll(pageable: Pageable?): Page<E>?

    fun findAll(): MutableIterable<E>?

    fun findById(id: I): E

    fun save(entity: E): E

    fun delete(id: I)
}