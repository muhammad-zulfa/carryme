package com.carryme.services

import com.carryme.dto.requests.FerryRequestDto
import com.carryme.entities.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IOperationTicketService: IBaseServices<OperationTicket,Long> {
}