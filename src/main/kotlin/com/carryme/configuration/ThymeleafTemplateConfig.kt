package com.carryme.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.templatemode.TemplateMode

import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver

import org.thymeleaf.spring5.SpringTemplateEngine
import java.nio.charset.StandardCharsets
import java.util.*

@Configuration
class ThymeleafTemplateConfig {
    @Bean
    fun springTemplateEngine(): SpringTemplateEngine? {
        val templateEngine = SpringTemplateEngine()
        templateEngine.addTemplateResolver(htmlTemplateResolver())
        return templateEngine
    }

    @Bean
    fun htmlTemplateResolver(): SpringResourceTemplateResolver? {
        val emailTemplateResolver = SpringResourceTemplateResolver()
        emailTemplateResolver.prefix = "classpath:/templates/"
        emailTemplateResolver.suffix = ".html"
        emailTemplateResolver.resolvablePatterns = Collections.singleton("html/*")
        emailTemplateResolver.templateMode = TemplateMode.HTML
        emailTemplateResolver.characterEncoding = "UTF-8"
        emailTemplateResolver.isCacheable = false
        return emailTemplateResolver
    }
}