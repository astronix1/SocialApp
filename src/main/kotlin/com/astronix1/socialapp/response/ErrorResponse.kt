package com.astronix1.socialapp.response

import com.astronix1.socialapp.error.ValidationError
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.util.Date

data class ErrorResponse(
    val statusCode: Int,
    val status: HttpStatus,
    val reason: String,
    val message: String,

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "MM-dd-yyyy hh:mm:ss",
        timezone = "Asia/Kolkata"
    )
    val timestamp: Date,

    val validationErrors: Map<String, List<ValidationError>> = emptyMap()
)
