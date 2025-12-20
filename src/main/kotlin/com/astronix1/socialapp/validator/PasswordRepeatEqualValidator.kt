package com.astronix1.socialapp.validator

import com.astronix1.socialapp.annotation.PasswordRepeatEqual
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class PasswordRepeatEqualValidator :
    ConstraintValidator<PasswordRepeatEqual, Any> {

    private lateinit var passwordFieldFirst: String
    private lateinit var passwordFieldSecond: String
    private lateinit var message: String

    override fun initialize(constraintAnnotation: PasswordRepeatEqual) {
        passwordFieldFirst = constraintAnnotation.passwordFieldFirst
        passwordFieldSecond = constraintAnnotation.passwordFieldSecond
        message = constraintAnnotation.message
    }

    override fun isValid(
        value: Any?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) return true

        try {
            val kClass = value::class

            val firstProp = kClass.declaredMemberProperties
                .first { it.name == passwordFieldFirst }
            val secondProp = kClass.declaredMemberProperties
                .first { it.name == passwordFieldSecond }

            firstProp.isAccessible = true
            secondProp.isAccessible = true

            val firstValue = firstProp.getter.call(value)
            val secondValue = secondProp.getter.call(value)

            val valid = firstValue == secondValue

            if (!valid) {
                context.disableDefaultConstraintViolation()
                context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(passwordFieldSecond)
                    .addConstraintViolation()
            }

            return valid
        } catch (ex: Exception) {
            return false
        }
    }
}
