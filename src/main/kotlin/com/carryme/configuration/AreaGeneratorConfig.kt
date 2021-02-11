package com.carryme.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "area")
class AreaGeneratorConfig {
    val generate: String? = null
}