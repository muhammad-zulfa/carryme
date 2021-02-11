package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.services.IDockService
import com.carryme.services.ISalesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/backoffice/dock")
@RestController
@CrossOrigin(maxAge = 3600,origins = ["*"])
class SalesController(
        @Autowired
        val service: ISalesService
) : BaseController() {
}