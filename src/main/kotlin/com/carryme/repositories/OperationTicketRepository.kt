package com.carryme.repositories

import com.carryme.entities.FerryDetail
import com.carryme.entities.Operation
import com.carryme.entities.OperationTicket
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface OperationTicketRepository: CrudRepository<OperationTicket,Long> {
    fun findAllByRoutesIdAndDeparture(routes: Long,departure: Date): List<OperationTicket>
    fun findAllByOperationId(id: Long): List<OperationTicket>
    fun findOneByRoutesIdAndOperationIdAndFerrySeatsIdAndFerryId(routeId: Long, operationId: Long, id: Long, id1: Long): OperationTicket
    fun findAllByOperationIdAndRoutesIdAndFerrySeatsId(operationId: Long,routeId: Long, seatId:Long): List<OperationTicket>

    @Query("select distinct(o.operation.id) from OperationTicket o where o.routes.id = :routes")
    fun findDistinctByRoutesId(@Param("routes") id: Long): List<Long>
}