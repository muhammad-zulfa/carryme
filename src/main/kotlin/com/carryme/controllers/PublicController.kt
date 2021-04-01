package com.carryme.controllers

import com.carryme.dto.response.BaseResponse
import com.carryme.entities.Operation
import com.carryme.services.IDockService
import com.carryme.services.IOperationTicketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/public")
class PublicController(
    @Autowired
    private val dockService: IDockService,
    @Autowired
    private val operationService: IOperationTicketService
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
        val pages: Page<Operation>? = operationService.findAllOperationByDepartureAndGuest(departure,guest,origin,destination,pgbl)
        return successResponse(pages)!!
    }

    @RequestMapping("/operation-ticket/{id}",method = [RequestMethod.GET])
    fun get(
        @PathVariable("id") id: Long
    ): BaseResponse {
        return successResponse(operationService.findOperationById(id))!!
    }
}