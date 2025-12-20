package com.astronix1.socialapp.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("default")
class EmailServiceDevImpl : EmailService {

    override fun send(to: String, subject: String, content: String) {
        println("DEV EMAIL (not sent)")
        println("To: $to")
        println("Subject: $subject")
        println("Content: $content")
    }

    override fun buildEmailVerifyMail(token: String): String {
        return "DEV VERIFY EMAIL TOKEN: $token"
    }

    override fun buildResetPasswordMail(token: String): String {
        return "DEV RESET PASSWORD TOKEN: $token"
    }
}
