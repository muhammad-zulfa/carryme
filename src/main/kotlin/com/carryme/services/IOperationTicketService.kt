package com.carryme.services

import com.carryme.dto.requests.BookSeatRequestDto
import com.carryme.dto.requests.OperationRequestDto
import com.carryme.dto.requests.UserData
import com.carryme.dto.response.OperationTicketSeatResponse
import com.carryme.dto.response.SalesResponseDto
import com.carryme.entities.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IOperationTicketService: IBaseServices<OperationTicket,Long> {
    fun findAllOperation(name: String, pgbl: Pageable): Page<Operation>
    fun submit(form: OperationRequestDto): Operation
    fun deleteAll(id: List<Long>)
    fun findOperationById(id: Long, origin: Long?, destination: Long?) : Operation
    fun findOperationTicketSeat(routeId: Long, operationId: Long, deckNumber: Int): Map<String,List<List<OperationTicketSeatResponse>>>
    fun bookSeat(id: Long, form: UserData): OperationTicket
    fun findAllOperationByDepartureAndGuest(
        departure: String,
        guest: Int,
        origin: Long,
        destination: Long,
        pgbl: Pageable
    ): List<Operation>?

    fun bookSeatFromCustomer(form: BookSeatRequestDto): SalesResponseDto?
    fun getReport(operationId: Long): String
}