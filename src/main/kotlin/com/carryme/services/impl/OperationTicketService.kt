package com.carryme.services.impl

import com.carryme.dto.requests.OperationRequestDto
import com.carryme.dto.requests.UserData
import com.carryme.dto.response.OperationTicketSeatResponse
import com.carryme.entities.*
import com.carryme.repositories.*
import com.carryme.services.IOperationTicketService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.days


@Service
class OperationTicketService: IOperationTicketService{
    @Autowired
    private lateinit var operationRepository: OperationRepository

    @Autowired
    private lateinit var operationTicketRepository: OperationTicketRepository

    @Autowired
    private lateinit var ferrySeatRepository: FerrySeatRepository

    @Autowired
    private lateinit var ferryRepository: FerryRepository

    @Autowired
    private lateinit var routeRepository: RouteRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var salesRepository: SalesRepository

    @Autowired
    private lateinit var ticketSalesRepository: TicketSalesRepository

    override fun findAllOperation(name: String, pgbl: Pageable): Page<Operation> {
        return operationRepository.findAllByFerryNameLike(name,pgbl)
    }

    override fun submit(form: OperationRequestDto): Operation {
        val operation = when(form.id){
            null -> Operation()
            else -> operationRepository.findById(form.id!!).get()
        }
        BeanUtils.copyProperties(form,operation)
        operation.ferry = ferryRepository.findById(form.ferry!!).get()
        operation.routes = routeRepository.findById(form.route!!).get()
        operationRepository.save(operation)

        //generate ticket
        val ticketOperation = operationTicketRepository.findAllByOperationId(operation.id)
        if (ticketOperation.isEmpty()){
            val seats = ferrySeatRepository.findAllByFerryIdOrderByDeckNumberAscSeatRowAscSeatCodeAsc(operation.ferry!!.id)
            val operationTicket: MutableList<OperationTicket> = mutableListOf()
            seats.forEach {
                operationTicket.add(OperationTicket().apply {
                    ferry = operation.ferry
                    ferrySeats = it
                    booked = false
                    price = operation.routes!!.price
                    routes = operation.routes
                    departure = operation.departure
                    this.operation = operation
                })
            }
            operationTicketRepository.saveAll(operationTicket)
        }else{
            ticketOperation.map {
                it.departure = operation.departure
            }
            operationTicketRepository.saveAll(ticketOperation)
        }
        return operation
    }

    override fun deleteAll(id: List<Long>) {
        operationRepository.deleteAll(operationRepository.findAllById(id))
    }

    override fun findOperationById(id: Long): Operation {
        return operationRepository.findById(id).get()
    }

    override fun findOperationTicketSeat(operationId: Long,deckNumber: Int): Map<String,List<List<OperationTicketSeatResponse>>> {
        val operation = operationRepository.findById(operationId).get()
        val seats = ferrySeatRepository.findAllByDeckNumberAndFerryIdOrderByDeckNumberAscSeatRowAscSeatCodeAsc(deckNumber,operation.ferry!!.id)
        val response: MutableList<OperationTicketSeatResponse> = mutableListOf()
        val mergedResponse: MutableList<List<OperationTicketSeatResponse>> = mutableListOf()
        var temp = ""

        val res : MutableMap<String,List<List<OperationTicketSeatResponse>>> = mutableMapOf()
        seats.forEach {
            val deck = "deck${it.deckNumber}"
            if(temp == "") temp = deck
            else if(temp != deck){
                res.put(temp,mergedResponse)
                mergedResponse.clear()
                temp = deck
            }
            val resp = OperationTicketSeatResponse()
            BeanUtils.copyProperties(it,resp)
            val status = operationTicketRepository.findOneByOperationIdAndFerrySeatsIdAndFerryId(operationId, it.id,operation.ferry!!.id)
            resp.status = OperationTicketSeatResponse.OperationStatus().apply {
                this.id = status.id
                this.booked = status.booked
            }
            response.add(resp)

            if(response.size == 4){
                mergedResponse.add(response.toList())
                response.clear()
                res.put(deck,mergedResponse)
            }
        }

        if(response.size > 0){
            mergedResponse.add(response.toList())
            res.put(temp,mergedResponse)
        }

        return res
    }

    override fun bookSeat(id: Long, form: UserData): OperationTicket{
        val ticket = operationTicketRepository.findById(id).get()
        val guest: User = User()
        BeanUtils.copyProperties(form,guest)
        userRepository.save(guest)
        val principal: UserDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val createdAt = Date()
        val creator: User = userRepository.findByUsername(principal.username)
        val sales: Sales = Sales().apply {
            status = "paid"
            totalPrice = ticket.price
            paymentMethod = "cash"
            createdBy = creator.id.toString()
            this.createdAt = createdAt
        }
        salesRepository.save(sales)

        val ticketSales: TicketSales = TicketSales().apply {
            this.ticket = ticket
            this.user = guest
            this.sales = sales
            createdBy = creator.id.toString()
            this.createdAt = createdAt
        }

        ticketSalesRepository.save(ticketSales)
        ticket.apply {
            booked = true
            bookedAt = createdAt
        }

        return operationTicketRepository.save(ticket)
    }

    override fun findAllOperationByDepartureAndGuest(
        departure: String,
        guest: Int,
        origin: Long,
        destination: Long,
        pgbl: Pageable
    ): Page<Operation>? {
        val route: Routes? = routeRepository.findByOriginIdAndDestinationId(origin,destination)
        if(route != null){
            val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            val now = Calendar.getInstance();
            val dep = SimpleDateFormat("yyyy-MM-dd").parse(departure)
            now.time = dep
            now.add(Calendar.HOUR,2)
            val coba: String = sdf.format(now.time)
            return operationRepository.findAllByRoutesIdAndDepartureGreaterThanEqual(route.id,now.time,pgbl)
        }else{
            return null;
        }
    }

    override fun findAll(pageable: Pageable?): Page<OperationTicket>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<OperationTicket>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): OperationTicket {
        return operationTicketRepository.findById(id).get()
    }

    override fun save(entity: OperationTicket): OperationTicket {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}