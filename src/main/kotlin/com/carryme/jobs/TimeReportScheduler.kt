package com.carryme.jobs

import com.carryme.services.ISalesService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*


@Component
class TimeReportScheduler {

    private val log: Logger = LoggerFactory.getLogger(TimeReportScheduler::class.java)

    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    @Autowired
    private lateinit var salesService: ISalesService


    @Scheduled(fixedRate = 1*60*1000)
    fun reportCurrentTime() {
        log.info("Checking for Expired Payment {}", dateFormat.format(Date()))
        salesService.restoreExpiredPayment()
    }
}