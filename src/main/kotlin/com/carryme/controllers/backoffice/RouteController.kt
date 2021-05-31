package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.dto.requests.RouteRequestDto
import com.carryme.dto.response.BaseResponse
import com.carryme.entities.Routes
import com.carryme.services.IRouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RequestMapping("/backoffice/route/transit")
@RestController
@CrossOrigin(maxAge = 3600,origins = ["*"])
@PreAuthorize("hasAnyRole('ADMIN')")
class RouteController(
        @Autowired
        val service: IRouteService
) : BaseController() {
        @RequestMapping("/{routeOperation}",method = [RequestMethod.GET])
        fun find(
                @PathVariable(value = "routeOperation") routeOperation: Long,
                @RequestParam(value = "search") search:String,
                @RequestParam(value = "pageNumber") page:Int,
                @RequestParam(value = "sortOrder") sortOrder:String,
                @RequestParam(value = "sortField") sortField:String,
                @RequestParam(value = "pageSize") pageSize:Int,
        ): BaseResponse {
                val sort: Sort = if(sortOrder == "desc") Sort.by(sortField).descending() else Sort.by(sortField).ascending()
                val pgbl: Pageable = PageRequest.of(page, pageSize, sort)
                val pages: Page<Routes> = service.findAllByName(routeOperation,"%$search%",pgbl)
                return successResponse(pages)!!
        }

        @RequestMapping("/submit",method = [RequestMethod.POST, RequestMethod.PUT])
        fun submit(
                @RequestBody form: RouteRequestDto
        ): BaseResponse {
                return successResponse(service.submit(form))!!
        }

        @RequestMapping("/detail/{id}",method = [RequestMethod.GET])
        fun get(
                @PathVariable("id") id: Long
        ): BaseResponse {
                return successResponse(service.findById(id))!!
        }

        @RequestMapping("delete",method = [RequestMethod.POST])
        fun delete(
                @RequestBody id: List<Long>
        ): BaseResponse {
                service.deleteAll(id)
                return successResponse(HttpStatus.OK)!!
        }

        @RequestMapping("/all/{id}",method = [RequestMethod.GET])
        fun findAll(
                @PathVariable("id") operation: Long
        ): BaseResponse {

                return successResponse(service.findAllByOperation(operation))!!
        }
}