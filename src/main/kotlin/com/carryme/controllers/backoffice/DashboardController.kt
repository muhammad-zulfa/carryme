package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.dto.response.BaseResponse
import com.carryme.services.ITicketSalesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/backoffice/dashboard")
@RestController
@CrossOrigin(maxAge = 3600,origins = ["*"])
class DashboardController(
    @Autowired
    val salesService: ITicketSalesService
): BaseController() {
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("")
    fun getData(): BaseResponse{
        val res = salesService.getDataCount();
        return successResponse(res)!!
    }
}