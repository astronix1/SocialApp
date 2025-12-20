package com.astronix1.socialapp.dto

import jakarta.validation.constraints.Size

data class PostDto(
    @field:Size(max = 4096)
    val content: String?
)
