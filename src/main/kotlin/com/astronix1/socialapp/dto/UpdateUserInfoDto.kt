package com.astronix1.socialapp.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class UpdateUserInfoDto(

    @field:NotEmpty
    @field:Size(max = 64)
    val firstName: String,

    @field:NotEmpty
    @field:Size(max = 64)
    val lastName: String,

    @field:Size(max = 100)
    val intro: String? = null,

    @field:Size(max = 16)
    val gender: String? = null,

    @field:Size(max = 64)
    val hometown: String? = null,

    @field:Size(max = 64)
    val currentCity: String? = null,

    @field:Size(max = 128)
    val eduInstitution: String? = null,

    @field:Size(max = 128)
    val workplace: String? = null,

    @field:Size(max = 64)
    val countryName: String? = null,

    @field:Past
    @field:DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val birthDate: LocalDateTime? = null
)
