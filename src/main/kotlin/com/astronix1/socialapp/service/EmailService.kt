package com.astronix1.socialapp.service

interface EmailService {
    fun send(to: String, subject: String, content: String)
    fun buildEmailVerifyMail(token: String): String
    fun buildResetPasswordMail(token: String): String
}
