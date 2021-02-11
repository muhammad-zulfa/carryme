package com.carryme.services

import com.carryme.dto.requests.FerryRequestDto
import com.carryme.entities.Ferry
import com.carryme.entities.FerryDetail
import com.carryme.entities.FerrySeats
import com.carryme.entities.Sales
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ISalesService: IBaseServices<Sales,Long> {
}