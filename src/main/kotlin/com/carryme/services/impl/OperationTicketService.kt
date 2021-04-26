package com.carryme.services.impl

import com.carryme.dto.requests.BookSeatRequestDto
import com.carryme.dto.requests.OperationRequestDto
import com.carryme.dto.requests.UserData
import com.carryme.dto.response.OperationTicketSeatResponse
import com.carryme.dto.response.SalesResponseDto
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
    private lateinit var routeRepository: OperationRouteRepository

    @Autowired
    private lateinit var detailRouteRepository: RouteRepository

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
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        operation.departure = format.parse(form.departure)
        operationRepository.save(operation)

        //generate ticket
        val ticketOperation = operationTicketRepository.findAllByOperationId(operation.id)
        if (ticketOperation.isEmpty()){
            val seats = ferrySeatRepository.findAllByFerryIdOrderByDeckNumberAscSeatRowAscSeatCodeAsc(operation.ferry!!.id)
            val operationTicket: MutableList<OperationTicket> = mutableListOf()
            val routes: List<Routes> = detailRouteRepository.findAllByOperationRoutesId(routeRepository.findById(form.route!!).get().id)
            routes.forEach {
                val r = it
                seats.forEach {
                    operationTicket.add(OperationTicket().apply {
                        ferry = operation.ferry
                        ferrySeats = it
                        booked = false
                        price = operation.routes!!.price
                        this.routes = r
                        departure = operation.departure
                        this.operation = operation
                    })
                }
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

    override fun findOperationById(id: Long, origin: Long?, destination: Long?): Operation {
        val operation =  operationRepository.findById(id).get()

        if(origin != null || destination != null){
            val routes = detailRouteRepository.findFirstByOperationRoutesIdAndOriginIdAndDestinationId(operation.routes!!.id,origin!!,destination!!)
            operation.apply {
                this.routes!!.id = routes.id
                this.routes!!.origin = routes.origin
                this.routes!!.destination = routes.destination
                this.routes!!.price = routes.price
                this.routes!!.eta = routes.eta
            }
        }
        return operation
    }

    override fun findOperationTicketSeat(routeId: Long, operationId: Long,deckNumber: Int): Map<String,List<List<OperationTicketSeatResponse>>> {
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
            val status = operationTicketRepository.findOneByRoutesIdAndOperationIdAndFerrySeatsIdAndFerryId(routeId,operationId, it.id,operation.ferry!!.id)
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
        val creator: User = userRepository.findByUsername(principal.username)!!
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
        bookDependedSeatAndSchedule(ticket.routes!!.origin!!.id,ticket.routes!!.destination!!.id,ticket)
        ticket.apply {
            booked = true
            bookedAt = createdAt
        }

        return operationTicketRepository.save(ticket)
    }

    private fun bookDependedSeatAndSchedule(originId: Long, destFinal:Long, ticket: OperationTicket){
        val routeDepend = detailRouteRepository.findAllByOperationRoutesIdAndOriginId(
            ticket.routes!!.operationRoutes!!.id,
            originId
        )
        routeDepend.map {
            val ticketDep = operationTicketRepository.findAllByOperationIdAndRoutesIdAndFerrySeatsId(ticket.operation!!.id,it.id,ticket.ferrySeats!!.id)
            ticketDep.forEach {
                it.apply {
                    booked = true
                    bookedAt = createdAt
                }
                operationTicketRepository.save(it)
                if(it.routes!!.destination!!.id != destFinal){
                    bookDependedSeatAndSchedule(it.routes!!.destination!!.id, destFinal,ticket)
                }
            }
        }
    }

    override fun findAllOperationByDepartureAndGuest(
        departure: String,
        guest: Int,
        origin: Long,
        destination: Long,
        pgbl: Pageable
    ): List<Operation>? {
        val routes: List<Routes>? = detailRouteRepository.findAllByOriginIdAndDestinationId(origin, destination)
        var schedule = mutableListOf<Operation>()
        if (routes!!.isNotEmpty()) {
            routes.forEach {
                val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                val now = Calendar.getInstance();
                val dep = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("$departure 07:00:00")
                val end = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("$departure 23:59:59")
                now.time = dep
                now.add(Calendar.HOUR, 2)
                val coba: String = sdf.format(now.time)

                val ticketOps: List<Long> = operationTicketRepository.findDistinctByRoutesId(it.id, now.time)

                ticketOps.map { p ->
                    val operationParent = operationRepository.findById(p).get().apply {
                        this.routes!!.price = it.price
                        this.routes!!.destination = it.destination

                        val d = Calendar.getInstance()
                        d.time = this.departure
                        if (this.routes!!.origin!!.id != it.origin!!.id) {
                            val transits =
                                detailRouteRepository.findFirstByOperationRoutesIdAndOriginIdAndDestinationId(
                                    this.id,
                                    it.origin!!.id,
                                    it.destination!!.id
                                )
                            d.add(Calendar.MINUTE, transits.eta!!)
                        }
                        this.departure = d.time
                        this.routes!!.eta = it.eta
                    }
                    schedule.add(operationParent)
                }
            }

            return schedule
        } else {
            return null;
        }
    }

    override fun bookSeatFromCustomer(form: BookSeatRequestDto): SalesResponseDto? {
        if(form.routeId == null || form.ferryId == null || form.operationId == null){
            throw Exception("No Operation Found")
        }else{
            var principal: UserDetails
            var creator: User
            if(SecurityContextHolder.getContext().authentication.principal != "anonymousUser"){
                principal = SecurityContextHolder.getContext().authentication.principal as UserDetails
                creator = userRepository.findByUsername(principal.username)!!
            }else{
                val check = userRepository.findByUsername(form.username)
                if (check != null){
                    creator = check
                }else{
                    creator = userRepository.save(User().apply {
                        fullname = form.fullname
                        username = form.username
                        phone = form.phone
                        isGuest = true
                        active = false
                    })
                }
            }
            var sales : Sales? = null
            var total = 0;
            val ticketSalesData = mutableListOf<TicketSales>()
            form.pax.forEach {
                val ticket = operationTicketRepository.findOneByRoutesIdAndOperationIdAndFerrySeatsIdAndFerryId(form.routeId!!,form.operationId!!,it.seatId!!,form.ferryId!!)
                val guest: User = User()
                BeanUtils.copyProperties(it,guest)
                userRepository.save(guest.apply {
                    guestUser = creator
                })
                val createdAt = Date()
                val exp = Calendar.getInstance()
                exp.time = createdAt
                exp.add(Calendar.MINUTE,30)
                if(sales == null) {
                    sales = salesRepository.save(Sales().apply {
                        status = "booked"
                        totalPrice = ticket.price
                        paymentMethod = "transfer"
                        createdBy = creator.id.toString()
                        paymentExpired = exp.time
                        this.createdAt = createdAt
                    })
                }

                val ticketSales: TicketSales = TicketSales().apply {
                    this.ticket = ticket
                    this.user = guest
                    this.sales = sales
                    createdBy = creator.id.toString()
                    this.createdAt = createdAt
                }

                ticketSalesData.add(ticketSales)
                total += sales!!.totalPrice!!

                ticketSalesRepository.save(ticketSales)
                bookDependedSeatAndSchedule(ticket.routes!!.origin!!.id,ticket.routes!!.destination!!.id,ticket)
                ticket.apply {
                    booked = true
                    bookedAt = createdAt
                }

                operationTicketRepository.save(ticket)

            }
            sales = salesRepository.save(sales!!.apply { totalPrice = total })

            return SalesResponseDto().apply {
                this.id = sales!!.id
                this.status = sales!!.status
                this.paymentExpired = sales!!.paymentExpired
                this.paymentMethod = sales!!.paymentMethod
                this.paymentProof = sales!!.paymentProof
                this.totalPrice = sales!!.totalPrice
            }
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