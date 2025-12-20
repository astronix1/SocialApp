package com.astronix1.socialapp.dto

import com.astronix1.socialapp.annotation.PasswordRepeatEqual
import com.astronix1.socialapp.annotation.ValidPassword

@PasswordRepeatEqual(
    passwordFieldFirst = "password",
    passwordFieldSecond = "passwordRepeat"
)
data class UpdatePasswordDto(
    @field:ValidPassword
    val password: String,

    val passwordRepeat: String,
    val oldPassword: String
)
