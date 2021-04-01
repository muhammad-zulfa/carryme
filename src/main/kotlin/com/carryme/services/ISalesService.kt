package com.carryme.services

import com.carryme.dto.response.SalesResponseDto
import com.carryme.entities.Sales
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile

interface ISalesService: IBaseServices<Sales,Long> {
    fun findAllByName(name: String, pgbl: Pageable): Page<Sales>
    fun submitProof(id: Long, file: MultipartFile): Sales
    fun detail(id: Long): SalesResponseDto
    fun updateStatus(id: Long, status: String): Sales
}