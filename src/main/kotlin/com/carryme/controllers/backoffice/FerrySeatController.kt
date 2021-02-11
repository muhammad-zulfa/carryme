package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.dto.requests.FerrySeatGenerateRequestDto
import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.dto.response.BaseResponse
import com.carryme.entities.FerrySeats
import com.carryme.services.IFerrySeatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/backoffice/ferry-seat")
@CrossOrigin(origins = ["*"],maxAge = 3600)
@RestController
class FerrySeatController(
        @Autowired
        val ferryService: IFerrySeatService
): BaseController() {

    @RequestMapping("",method = [RequestMethod.GET])
    fun findByShipId(
            @RequestParam(value = "deckNumber") deck:Int,
            @RequestParam("shipId") shipId: Long
    ):BaseResponse{
        val pages: Map<String, List<List<FerrySeats>>> = ferryService.findAllByShipIdAndDeckNumber(shipId, deck)
        return successResponse(pages)!!
    }

    @RequestMapping("/submit/{shipId}",method = [RequestMethod.POST,RequestMethod.PUT])
    fun submit(
            @PathVariable("shipId") shipId: Long,
            @RequestBody form: FerrySeatRequestDto
    ):BaseResponse{
        return successResponse(ferryService.submit(shipId,form))!!
    }

    @RequestMapping("/generate/{id}",method = [RequestMethod.POST,RequestMethod.PUT])
    fun submit(
            @RequestBody form: FerrySeatGenerateRequestDto,
            @PathVariable("id") shipId: Long
    ):BaseResponse{
        return successResponse(ferryService.generate(shipId,form))!!
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