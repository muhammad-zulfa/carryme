package com.carryme.repositories

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

    @Query(
        value = "select count(o.operation.id) from OperationTicket o where o.operation.id = :operationId and o.booked = false and o.routes.origin.id = :origin and o.routes.destination.id = :dest"
    )
    fun findCountAvailableSeatByOperationId(@Param("operationId") operationId: Long,@Param("origin") origin: Long,@Param("dest") dest: Long): Long

    @Query("select distinct(o.operation.id),o.operation.departure from OperationTicket o where o.routes.id = :routes and (o.operation.departure < :end and o.operation.departure > :dep) order by o.operation.departure asc ")
    fun findDistinctByRoutesId(@Param("routes") id: Long, @Param("dep") dep: Date, end: Date): List<Long>
}