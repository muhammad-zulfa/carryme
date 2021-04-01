package com.carryme.services.impl

import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.dto.response.SalesResponseDto
import com.carryme.entities.FerrySeats
import com.carryme.entities.Sales
import com.carryme.repositories.FerrySeatRepository
import com.carryme.repositories.SalesRepository
import com.carryme.repositories.TicketSalesRepository
import com.carryme.services.IFerryDetailService
import com.carryme.services.ISalesService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*
import java.io.FileOutputStream

import java.io.BufferedOutputStream
import java.io.File
import java.lang.Exception


@Service
class SalesService: ISalesService{
    @Autowired
    private lateinit var repository: SalesRepository
    @Autowired
    private lateinit var ticketSalesRepository: TicketSalesRepository

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
                        null -> "upload/${salesEntity.id}"
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
        return response.apply { this.ticketSales = ticketSales }
    }

    override fun updateStatus(id: Long, status: String): Sales {
        val now = Date()
        val salesEntity = repository.findById(id).get()
        salesEntity.apply {
            this.status = status
            this.updatedAt = now
        }
        return repository.save(salesEntity)
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