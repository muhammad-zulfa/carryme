package com.carryme.services.impl

import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.dto.requests.Mail
import com.carryme.dto.requests.UserData
import com.carryme.dto.response.SalesResponseDto
import com.carryme.entities.FerrySeats
import com.carryme.entities.OperationTicket
import com.carryme.entities.Sales
import com.carryme.entities.User
import com.carryme.repositories.*
import com.carryme.services.EmailSenderService
import com.carryme.services.IFerryDetailService
import com.carryme.services.ISalesService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.util.*
import java.io.FileOutputStream

import java.io.BufferedOutputStream
import java.io.File
import java.lang.Exception
import com.carryme.utils.FileUploadUtil
import com.carryme.utils.SetupDataLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Service
class SalesService: ISalesService{
    @Autowired
    private lateinit var repository: SalesRepository
    @Autowired
    private lateinit var ticketSalesRepository: TicketSalesRepository
    @Autowired
    private lateinit var operationTicketRepository: OperationTicketRepository
    @Autowired
    private lateinit var detailRouteRepository: RouteRepository
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private val emailService: EmailSenderService? = null

    private val log: Logger = LoggerFactory.getLogger(SetupDataLoader::class.java)

    override fun findAllByName(name: String, pgbl: Pageable): Page<Sales> {
        return repository.findAllByStatusLike(name,pgbl)
    }

    override fun submitProof(id: Long, file: MultipartFile): Sales {
        val sales = repository.findById(id)
        val now = Date()
        if(sales.isPresent) {
            if (!file.isEmpty) {
                try {
                    val salesEntity = sales.get();
                    val pathName = when(salesEntity.paymentProof){
                        null -> "upload/payment-proof/${salesEntity.id}"
                        else -> salesEntity.paymentProof
                    }
                    val path = File("classpath:$pathName/")
                    if (!path.exists()){
                        path.mkdirs()
                    }
                    val bytes = file.bytes
                    val stream = BufferedOutputStream(FileOutputStream(File(path,"proof.jpg")))
                    stream.write(bytes)
                    stream.close()

                    salesEntity.apply {
                        status = "paid"
                        paymentProof = "$pathName/proof"
                        this.updatedAt = now
                    }
                    repository.save(salesEntity)
                    return salesEntity
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception("File Is Empty")
            }

        }else{
            throw Exception("Data Not Found")
        }
    }

    override fun detail(id: Long): SalesResponseDto {
        val response = SalesResponseDto()
        BeanUtils.copyProperties(repository.findById(id).get(), response)
        val ticketSales = ticketSalesRepository.findAllBySalesId(id)
        response.paymentProof = "upload/payment-proof/$id/${response.paymentProof}"
        ticketSales.forEach {
            it.ticket!!.operation = null
        }
        return response.apply { this.ticketSales = ticketSales }
    }

    override fun updateStatus(id: Long, status: String): Sales {
        val now = Date()
        val salesEntity = repository.findById(id).get()
        salesEntity.apply {
            this.status = status
            this.updatedAt = now
        }
        val user = userRepository.findById(salesEntity.createdBy!!.toLong())
        var userEntity: User
        if (!user.isPresent){
            throw Exception("User Not Found!!")
        }else{
            userEntity = user.get()
        }
        if(status == "finish"){
            log.info("START... Sending email")
            val model: MutableMap<String, Any> = HashMap()
            model["name"] = "Developer!"
            model["location"] = "United States"
            model["sign"] = "Java Developer"
            val mail = Mail(
                from = "no-reply@zulfa.tech",
                to = userEntity.username!!,
                subject = "Voucher Issued (E-ticket Dishub)",
                template = "e-ticket",
                props = model
            )
            emailService!!.sendEmail(mail)
            log.info("END... Email sent success")
        }
        return repository.save(salesEntity)
    }

    override fun restoreExpiredPayment() {
        val now = Date()
        val sales = repository.findAllByStatusAndPaymentExpiredLessThanEqual("booked",now)

        if(sales.isNotEmpty()){
            sales.forEach {
                it.status = "payment_expired"
                val salesTickets = ticketSalesRepository.findAllBySalesId(it.id)
                salesTickets.forEach { d ->
                    val opT = operationTicketRepository.findById(d.ticket!!.id).get()
                    opT.apply {
                        booked = false
                        bookedAt = null
                    }
                    operationTicketRepository.save(opT)
                    reOpenDependedSeatAndSchedule(
                        d.ticket!!.routes!!.origin!!.id,
                        d.ticket!!.routes!!.destination!!.id,
                        opT
                    )
                }
                repository.save(it)
            }
        }
    }

    override fun uploadPaymentProof(id: Long, file: MultipartFile): Sales {
        val fileName: String = StringUtils.cleanPath(file.originalFilename!!)
        val uploadDir = "upload/payment-proof/${id}"
        FileUploadUtil.saveFile(uploadDir, fileName, file)
        val sales = repository.findById(id).get()
        sales.paymentProof = fileName
        sales.status = "paid"
        repository.save(sales)
        return sales
    }

    private fun reOpenDependedSeatAndSchedule(originId: Long, destFinal:Long, ticket: OperationTicket){
        val routeDepend = detailRouteRepository.findAllByOperationRoutesIdAndOriginId(
            ticket.routes!!.operationRoutes!!.id,
            originId
        )
        routeDepend.map {
            val ticketDep = operationTicketRepository.findAllByOperationIdAndRoutesIdAndFerrySeatsId(ticket.operation!!.id,it.id,ticket.ferrySeats!!.id)
            ticketDep.forEach {
                it.apply {
                    booked = false
                    bookedAt = null
                }
                operationTicketRepository.save(it)
                if(it.routes!!.destination!!.id != destFinal){
                    reOpenDependedSeatAndSchedule(it.routes!!.destination!!.id, destFinal,ticket)
                }
            }
        }
    }

    override fun findAll(pageable: Pageable?): Page<Sales>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<Sales>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Sales {
        TODO("Not yet implemented")
    }

    override fun save(entity: Sales): Sales {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}