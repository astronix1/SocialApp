package com.astronix1.socialapp.dto

import com.astronix1.socialapp.annotation.PasswordRepeatEqual
import com.astronix1.socialapp.annotation.ValidEmail
import com.astronix1.socialapp.annotation.ValidPassword
import jakarta.validation.constraints.Size

@PasswordRepeatEqual(
    passwordFieldFirst = "password",
    passwordFieldSecond = "passwordRepeat"
)
data class SignupDto(

    @field:ValidEmail
    val email: String,

    @field:ValidPassword
    val password: String,

    val passwordRepeat: String,

    @field:Size(max = 64)
    val firstName: String = "",

    @field:Size(max = 64)
    val lastName: String = ""
)