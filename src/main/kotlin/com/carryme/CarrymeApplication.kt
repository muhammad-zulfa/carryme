package com.carryme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.carryme"])
@EnableScheduling
class CarrymeApplication

fun main(args: Array<String>) {
    runApplication<CarrymeApplication>(*args)
}
