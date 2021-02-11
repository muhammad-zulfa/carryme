package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.dto.requests.FerryRequestDto
import com.carryme.dto.response.BaseResponse
import com.carryme.entities.Ferry
import com.carryme.services.IFerryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/backoffice/ferry")
@CrossOrigin(origins = ["*"],maxAge = 3600)
@RestController
class FerryController(
        @Autowired
        val ferryService: IFerryService
): BaseController() {

    @RequestMapping("",method = [RequestMethod.GET])
    fun find(
            @RequestParam(value = "search") search:String,
            @RequestParam(value = "pageNumber") page:Int,
            @RequestParam(value = "sortOrder") sortOrder:String,
            @RequestParam(value = "sortField") sortField:String,
            @RequestParam(value = "pageSize") pageSize:Int,
    ):BaseResponse{
        val sort: Sort = if(sortOrder == "desc") Sort.by(sortField).descending() else Sort.by(sortField).ascending()
        val pgbl: Pageable = PageRequest.of(page, pageSize, sort)
        val pages: Page<Ferry> = ferryService.findAllByName("%$search%",pgbl)
        return successResponse(pages)!!
    }

    @RequestMapping("/submit",method = [RequestMethod.POST,RequestMethod.PUT])
    fun submit(
            @RequestBody form: FerryRequestDto
    ):BaseResponse{
        return successResponse(ferryService.submit(form))!!
    }

    @RequestMapping("/{id}",method = [RequestMethod.GET])
    fun get(
            @PathVariable("id") id: Long
    ):BaseResponse{
        return successResponse(ferryService.findById(id))!!
    }

    @RequestMapping("delete/{id}",method = [RequestMethod.DELETE])
    fun delete(
            @PathVariable("id") id: Long
    ):BaseResponse{
        ferryService.delete(id)
        return successResponse(HttpStatus.OK)!!
    }
}