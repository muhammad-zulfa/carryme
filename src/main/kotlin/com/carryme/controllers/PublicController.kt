package com.carryme.controllers

import com.carryme.dto.requests.BookSeatRequestDto
import com.carryme.dto.requests.UserData
import com.carryme.dto.response.BaseResponse
import com.carryme.entities.Operation
import com.carryme.services.IDockService
import com.carryme.services.IOperationTicketService
import com.carryme.services.ISalesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/public")
class PublicController(
    @Autowired
    private val dockService: IDockService,
    @Autowired
    private val operationService: IOperationTicketService,
    @Autowired
    private val salesService: ISalesService
):  BaseController() {

    @GetMapping("/docks")
    fun getDocks(): BaseResponse{
        return successResponse(dockService.findAll())!!
    }
    @RequestMapping("/operation-ticket",method = [RequestMethod.GET])
    fun find(
        @RequestParam(value = "departure") departure:String,
        @RequestParam(value = "guest") guest: Int,
        @RequestParam(value = "origin") origin: Long,
        @RequestParam(value = "destination") destination: Long,
        @RequestParam(value = "pageNumber") page:Int,
    ): BaseResponse {
        val sort: Sort = Sort.by("departure").ascending()
        val pgbl: Pageable = PageRequest.of(page, 10, sort)
        val pages: List<Operation>? = operationService.findAllOperationByDepartureAndGuest(departure,guest,origin,destination,pgbl)
        return successResponse(pages)!!
    }

    @RequestMapping("/operation-ticket/{id}",method = [RequestMethod.GET])
    fun get(
        @PathVariable("id") id: Long,
        @RequestParam(value = "origin") origin: Long?,
        @RequestParam(value = "destination") destination: Long?,
    ): BaseResponse {
        return successResponse(operationService.findOperationById(id, origin, destination))!!
    }

    @RequestMapping("/seat-operation/{id}",method = [RequestMethod.GET])
    fun seatOperation(
        @PathVariable id: Long,
        @RequestParam deckNumber: Int,
        @RequestParam routeId: Long
    ): BaseResponse {
        return successResponse(operationService.findOperationTicketSeat(routeId, id,deckNumber))!!
    }

    @RequestMapping("/book-seat",method = [RequestMethod.POST])
    fun bookSeat(
        @RequestBody form: BookSeatRequestDto
    ): BaseResponse {
        return successResponse(operationService.bookSeatFromCustomer(form))!!
    }

    @RequestMapping("/sales/{id}", method = [RequestMethod.GET])
    fun get(
        @PathVariable("id") id: Long
    ): BaseResponse {
        return successResponse(salesService.detail(id))!!
    }

    @RequestMapping("/sales/{id}", method = [RequestMethod.POST])
    fun postPaymentProof(
        @PathVariable("id") id: Long,
        @RequestParam("image") multipartFile: MultipartFile
    ): BaseResponse {
        return successResponse(salesService.uploadPaymentProof(id,multipartFile))!!
    }
}