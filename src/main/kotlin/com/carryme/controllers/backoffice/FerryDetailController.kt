package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.dto.requests.FerryDetailRequestDto
import com.carryme.dto.response.BaseResponse
import com.carryme.services.IFerryDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RequestMapping("/backoffice/ferry-detail")
@CrossOrigin(origins = ["*"],maxAge = 3600)
@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
class FerryDetailController(
        @Autowired
        val service: IFerryDetailService
): BaseController() {

        @RequestMapping("/{shipId}",method = [RequestMethod.GET])
        fun findByShipId(
                @PathVariable("shipId") shipId: Long
        ):BaseResponse{
                return successResponse(service.findByShipId(shipId))!!

        }

        @RequestMapping("/submit",method = [RequestMethod.POST,RequestMethod.PUT])
        fun submit(
                @RequestBody form: FerryDetailRequestDto
        ):BaseResponse{
                return successResponse(service.submit(form))!!
        }
}