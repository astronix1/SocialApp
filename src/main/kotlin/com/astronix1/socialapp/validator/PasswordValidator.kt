package com.astronix1.socialapp.validator

import com.astronix1.socialapp.annotation.ValidPassword
import com.astronix1.socialapp.common.AppConstants
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class PasswordValidator : ConstraintValidator<ValidPassword, String> {

    override fun isValid(
        password: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (password.isNullOrBlank()) return false

        val pattern = Pattern.compile(AppConstants.PASSWORD_PATTERN)
        return pattern.matcher(password).matches()
    }
}
