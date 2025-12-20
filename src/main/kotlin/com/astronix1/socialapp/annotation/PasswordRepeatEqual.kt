package com.astronix1.socialapp.annotation

import com.astronix1.socialapp.validator.PasswordRepeatEqualValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordRepeatEqualValidator::class])
annotation class PasswordRepeatEqual(
    val message: String = "Password mismatch",
    val passwordFieldFirst: String,
    val passwordFieldSecond: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
