package com.carryme.services

import com.carryme.dto.requests.OperationRequestDto
import com.carryme.dto.requests.UserData
import com.carryme.dto.response.OperationTicketSeatResponse
import com.carryme.entities.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IOperationTicketService: IBaseServices<OperationTicket,Long> {
    fun findAllOperation(name: String, pgbl: Pageable): Page<Operation>
    fun submit(form: OperationRequestDto): Operation
    fun deleteAll(id: List<Long>)
    fun findOperationById(id: Long) : Operation
    fun findOperationTicketSeat(operationId: Long, deckNumber: Int): Map<String,List<List<OperationTicketSeatResponse>>>
    fun bookSeat(id: Long, form: UserData): OperationTicket
}