package com.astronix1.socialapp.dto

import com.astronix1.socialapp.annotation.ValidEmail
import com.astronix1.socialapp.annotation.ValidPassword

data class LoginDto(
    @field:ValidEmail
    val email: String,

    @field:ValidPassword
    val password: String
)
