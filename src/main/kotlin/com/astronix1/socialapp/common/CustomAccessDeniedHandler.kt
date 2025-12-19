package com.astronix1.socialapp.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.astronix1.socialapp.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.*

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AccessDeniedException
    ) {
        val errorResponse = ErrorResponse(
            statusCode = HttpStatus.UNAUTHORIZED.value(),
            status = HttpStatus.UNAUTHORIZED,
            message = AppConstants.ACCESS_DENIED,
            reason = HttpStatus.UNAUTHORIZED.reasonPhrase,
            timestamp = Date()
        )

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        ObjectMapper().writeValue(response.outputStream, errorResponse)
    }
}
