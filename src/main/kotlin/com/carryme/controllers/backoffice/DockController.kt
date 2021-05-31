package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.dto.requests.DockRequestDto
import com.carryme.dto.requests.FerryRequestDto
import com.carryme.dto.response.BaseResponse
import com.carryme.entities.Docks
import com.carryme.entities.Ferry
import com.carryme.services.IDockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RequestMapping("/backoffice/dock")
@RestController
@CrossOrigin(maxAge = 3600,origins = ["*"])
@PreAuthorize("hasAnyRole('ADMIN')")
class DockController(
        @Autowired
        val service: IDockService
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
                val pages: Page<Docks> = service.findAllByName("%$search%",pgbl)
                return successResponse(pages)!!
        }

        @RequestMapping("/submit",method = [RequestMethod.POST, RequestMethod.PUT])
        fun submit(
                @RequestBody form: DockRequestDto
        ): BaseResponse {
                return successResponse(service.submit(form))!!
        }

        @RequestMapping("/{id}",method = [RequestMethod.GET])
        fun get(
                @PathVariable("id") id: Long
        ): BaseResponse {
                return successResponse(service.findById(id))!!
        }

        @RequestMapping("delete", method = [RequestMethod.POST])
        fun delete(
                @RequestBody ids: List<Long>
        ): BaseResponse {

                service.deleteAll(ids)
                return successResponse(HttpStatus.OK)!!
        }

        @RequestMapping("/all",method = [RequestMethod.GET])
        fun findAll(

        ): BaseResponse {

                return successResponse(service.findAll())!!
        }

}