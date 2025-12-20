package com.astronix1.socialapp.dto

import jakarta.validation.constraints.NotEmpty

data class TagDto(
    @field:NotEmpty
    val tagName: String,

    @field:NotEmpty
    val action: String
)
