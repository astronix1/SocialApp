package com.astronix1.socialapp.annotation

import com.astronix1.socialapp.validator.EmailValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EmailValidator::class])
annotation class ValidEmail(
    val message: String = "Invalid email address",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
