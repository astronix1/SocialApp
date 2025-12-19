package com.astronix1.socialapp.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.astronix1.socialapp.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.*

@Component
class CustomAuthenticationEntryPoint : Http403ForbiddenEntryPoint() {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val errorResponse = ErrorResponse(
            statusCode = HttpStatus.FORBIDDEN.value(),
            status = HttpStatus.FORBIDDEN,
            message = AppConstants.FORBIDDEN,
            reason = HttpStatus.FORBIDDEN.reasonPhrase,
            timestamp = Date()
        )


        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.FORBIDDEN.value()
        ObjectMapper().writeValue(response.outputStream, errorResponse)
    }
}
