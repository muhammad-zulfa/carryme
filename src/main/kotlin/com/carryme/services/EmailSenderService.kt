package com.carryme.services

import com.carryme.dto.requests.Mail
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.MimeMessageHelper
import javax.mail.MessagingException
import org.thymeleaf.spring5.SpringTemplateEngine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import java.io.IOException
import java.nio.charset.StandardCharsets


@Service
class EmailSenderService {
    @Autowired
    private val emailSender: JavaMailSender? = null

    @Autowired
    private val templateEngine: SpringTemplateEngine? = null

    @Throws(MessagingException::class, IOException::class)
    fun sendEmail(mail: Mail) {
        val message = emailSender!!.createMimeMessage()
        try {
            val helper = MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
            )
            //helper.addAttachment("template-cover.png", ClassPathResource("javabydeveloper-email.PNG"))
            val context = Context()
            context.setVariables(mail.props)
            val html = templateEngine!!.process("html/${mail.template}", context)
            helper.setTo(mail.to)
            helper.setText(html, true)
            helper.setSubject(mail.subject)
            helper.setFrom(mail.from)
            emailSender.send(message)
        }catch (e: MessagingException){
            print("error ${e.message}")
        }
    }
}