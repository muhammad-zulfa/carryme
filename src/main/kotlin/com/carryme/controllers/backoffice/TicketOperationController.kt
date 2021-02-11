package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.services.IDockService
import com.carryme.services.IOperationTicketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/backoffice/ticket-operation")
@RestController
@CrossOrigin(maxAge = 3600,origins = ["*"])
class TicketOperationController(
        @Autowired
        val service: IOperationTicketService
) : BaseController() {
}