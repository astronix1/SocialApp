package com.astronix1.socialapp.service

import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Profile("prod")
@Transactional
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    private val environment: Environment
) : EmailService {

    @Async
    override fun send(to: String, subject: String, content: String) {
        try {
            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, "UTF-8")
            helper.setText(content, true)
            helper.setTo(to)
            helper.setSubject(subject)
            mailSender.send(message)
        } catch (e: MessagingException) {
            throw IllegalStateException("Failed to send email")
        }
    }

    override fun buildEmailVerifyMail(token: String): String {
        val url = "${environment.getProperty("app.root.frontend")}/verify-email/$token"
        return buildEmailBody(
            url,
            "Verify Email Address",
            "Please, click on the link below to verify your email address.",
            "Click to Verify"
        )
    }

    override fun buildResetPasswordMail(token: String): String {
        val url = "${environment.getProperty("app.root.frontend")}/reset-password/$token"
        return buildEmailBody(
            url,
            "Reset Your Password",
            "Please, click on the link below to get a new password.",
            "Get New Password"
        )
    }

    private fun buildEmailBody(
        url: String,
        header: String,
        detail: String,
        buttonText: String
    ): String {
        return """
            <div style="margin:0 auto;width:500px;text-align:center;">
                <h2>$header</h2>
                <p>$detail</p>
                <a href="$url">$buttonText</a>
            </div>
        """.trimIndent()
    }
}
