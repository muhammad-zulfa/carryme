package com.carryme.repositories

import com.carryme.entities.FerryDetail
import com.carryme.entities.OperationTicket
import org.springframework.data.repository.CrudRepository
import java.util.*

interface OperationTicketRepository: CrudRepository<OperationTicket,Long> {
    fun findAllByRoutesIdAndDeparture(routes: Long,departure: Date): List<OperationTicket>
}