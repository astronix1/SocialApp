package com.astronix1.socialapp.common

import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.astronix1.socialapp.error.ValidationError
import com.astronix1.socialapp.exception.*
import com.astronix1.socialapp.response.ErrorResponse
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.*
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.IOException
import java.util.*

@RestControllerAdvice
class CustomExceptionHandler {

    private fun buildErrorResponse(
        status: HttpStatus,
        message: String,
        validationErrors: Map<String, List<ValidationError>>? = null
    ): ResponseEntity<ErrorResponse> {

        val errorResponse = ErrorResponse(
            statusCode = status.value(),
            status = status,
            reason = status.reasonPhrase,
            message = message,
            timestamp = Date(),
            validationErrors = validationErrors ?: emptyMap()
        )

        return ResponseEntity(errorResponse, status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(
        e: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {

        val errors = mutableMapOf<String, MutableList<ValidationError>>()

        e.bindingResult.fieldErrors.forEach { fieldError ->
            val list = errors.getOrPut(fieldError.field) { mutableListOf() }
            list.add(
                ValidationError(
                    code = fieldError.code,
                    message = fieldError.defaultMessage
                )
            )
        }

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Validation Error",
            errors
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalServerError() =
        buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            AppConstants.INTERNAL_SERVER_ERROR
        )

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied() =
        buildErrorResponse(
            HttpStatus.UNAUTHORIZED,
            AppConstants.ACCESS_DENIED
        )

    @ExceptionHandler(InvalidOperationException::class)
    fun handleInvalidOperation() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.INVALID_OPERATION
        )

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.INCORRECT_CREDENTIALS
        )

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowed(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        val method: HttpMethod = e.supportedHttpMethods?.first() ?: HttpMethod.GET
        return buildErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED,
            String.format(AppConstants.METHOD_NOT_ALLOWED, method)
        )
    }

    @ExceptionHandler(IOException::class)
    fun handleIOException() =
        buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            AppConstants.FILE_PROCESSING_ERROR
        )

    @ExceptionHandler(LockedException::class)
    fun handleLocked() =
        buildErrorResponse(
            HttpStatus.FORBIDDEN,
            AppConstants.ACCOUNT_LOCKED
        )

    @ExceptionHandler(DisabledException::class)
    fun handleDisabled() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.ACCOUNT_DISABLED
        )

    @ExceptionHandler(TokenExpiredException::class, SignatureVerificationException::class)
    fun handleInvalidToken() =
        buildErrorResponse(
            HttpStatus.UNAUTHORIZED,
            AppConstants.INVALID_TOKEN
        )

    @ExceptionHandler(AuthenticationServiceException::class)
    fun handleAuthenticationServiceException() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.INCORRECT_CREDENTIALS
        )

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound() =
        buildErrorResponse(
            HttpStatus.NOT_FOUND,
            AppConstants.USER_NOT_FOUND
        )

    @ExceptionHandler(PostNotFoundException::class)
    fun handlePostNotFound() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.POST_NOT_FOUND
        )

    @ExceptionHandler(TagNotFoundException::class)
    fun handleTagNotFound() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.TAG_NOT_FOUND
        )

    @ExceptionHandler(TagExistsException::class)
    fun handleTagExists() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.TAG_EXISTS
        )

    @ExceptionHandler(CountryNotFoundException::class)
    fun handleCountryNotFound() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.COUNTRY_NOT_FOUND
        )

    @ExceptionHandler(CountryExistsException::class)
    fun handleCountryExists() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.COUNTRY_EXISTS
        )

    @ExceptionHandler(EmptyPostException::class)
    fun handleEmptyPost() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.EMPTY_POST
        )

    @ExceptionHandler(ShareNotFoundException::class)
    fun handleShareNotFound() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.SHARE_NOT_FOUND
        )

    @ExceptionHandler(ShareExistsException::class)
    fun handleShareExists() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.SHARE_EXISTS
        )

    @ExceptionHandler(DuplicateShareException::class)
    fun handleDuplicateShare() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.DUPLICATE_SHARE
        )

    @ExceptionHandler(NotificationNotFoundException::class)
    fun handleNotificationNotFound() =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            AppConstants.NOTIFICATION_NOT_FOUND
        )
}
