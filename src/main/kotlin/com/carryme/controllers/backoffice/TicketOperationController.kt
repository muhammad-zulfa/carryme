package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.dto.requests.OperationRequestDto
import com.carryme.dto.requests.RouteRequestDto
import com.carryme.dto.requests.UserData
import com.carryme.dto.response.BaseResponse
import com.carryme.entities.Operation
import com.carryme.services.IOperationTicketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/backoffice/ticket-operation")
@RestController
@CrossOrigin(maxAge = 3600,origins = ["*"])
class TicketOperationController(
        @Autowired
        val service: IOperationTicketService
) : BaseController() {
        @RequestMapping("",method = [RequestMethod.GET])
        fun find(
                @RequestParam(value = "search") search:String,
                @RequestParam(value = "pageNumber") page:Int,
                @RequestParam(value = "sortOrder") sortOrder:String,
                @RequestParam(value = "sortField") sortField:String,
                @RequestParam(value = "pageSize") pageSize:Int,
        ): BaseResponse {
                val sort: Sort = if(sortOrder == "desc") Sort.by(sortField).descending() else Sort.by(sortField).ascending()
                val pgbl: Pageable = PageRequest.of(page, pageSize, sort)
                val pages: Page<Operation> = service.findAllOperation("%$search%",pgbl)
                return successResponse(pages)!!
        }

        @RequestMapping("/submit",method = [RequestMethod.POST, RequestMethod.PUT])
        fun submit(
                @RequestBody form: OperationRequestDto
        ): BaseResponse {
                return successResponse(service.submit(form))!!
        }

        @RequestMapping("/{id}",method = [RequestMethod.GET])
        fun get(
                @PathVariable("id") id: Long
        ): BaseResponse {
                return successResponse(service.findOperationById(id))!!
        }

        @RequestMapping("delete",method = [RequestMethod.POST])
        fun delete(
                @RequestBody id: List<Long>
        ): BaseResponse {
                service.deleteAll(id)
                return successResponse(HttpStatus.OK)!!
        }

        @RequestMapping("seat-operation/{id}",method = [RequestMethod.GET])
        fun seatOperation(
                @PathVariable id: Long,
                @RequestParam deckNumber: Int
        ): BaseResponse {
                return successResponse(service.findOperationTicketSeat(id,deckNumber))!!
        }

        @RequestMapping("book-seat/{id}",method = [RequestMethod.POST])
        fun bookSeat(
                @PathVariable id: Long,
                @RequestBody form: UserData
        ): BaseResponse {
                return successResponse(service.bookSeat(id,form))!!
        }
}