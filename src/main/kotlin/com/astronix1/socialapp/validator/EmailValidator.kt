package com.astronix1.socialapp.validator

import com.astronix1.socialapp.annotation.ValidEmail
import com.astronix1.socialapp.common.AppConstants
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class EmailValidator : ConstraintValidator<ValidEmail, String> {

    override fun isValid(
        email: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (email.isNullOrBlank()) return false

        val pattern = Pattern.compile(AppConstants.EMAIL_PATTERN)
        return pattern.matcher(email).matches() &&
                email.length in 4..64
    }
}
