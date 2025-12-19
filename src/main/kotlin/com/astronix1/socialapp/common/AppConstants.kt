package com.astronix1.socialapp.common

object AppConstants {

    const val FOLLOWER_PER_PAGE = 20
    const val FOLLOWING_PER_PAGE = 20
    const val POST_PER_PAGE = 20
    const val CONTENT_PER_PAGE = 20
    const val MAX_POST_TAGS = 5

    const val JWT_EXPIRATION_2Wk: Long = 14 * 86_400_000L
    const val JWT_EXPIRATION_1d: Long = 86_400_000L

    const val TOKEN_PREFIX = "Bearer "
    const val TOKEN_HEADER = "Jwt-Token"
    const val OPTIONS_HTTP_METHOD = "options"
    const val AUTHORITIES = "authorities"

    const val TOKEN_UNVERIFIABLE = "Token cannot be verified."
    const val INVALID_TOKEN = "Token is not valid."
    const val FORBIDDEN = "You need to be logged in to access this resource."
    const val INVALID_OPERATION = "You cannot perform this operation."
    const val ACCESS_DENIED = "You don't have permission to access this resource."
    const val ACCOUNT_LOCKED = "Your account has been locked."
    const val METHOD_NOT_ALLOWED = "This operation is not allowed. Only %s operations are allowed."
    const val NOT_FOUND_ERROR = "404 Not Found."
    const val INTERNAL_SERVER_ERROR = "An error occurred while processing your request."
    const val INCORRECT_CREDENTIALS = "Incorrect username or password."
    const val ACCOUNT_DISABLED = "Your account has been disabled."
    const val FILE_PROCESSING_ERROR = "There was an error while processing your file."
    const val NOT_ENOUGH_PERMISSION = "You do not have enough permission to perform this action."

    const val VERIFY_EMAIL = "Verify your email"
    const val RESET_PASSWORD = "Reset your password"

    val PUBLIC_URLS = arrayOf(
        "/api/v1/signup",
        "/api/v1/login",
        "/api/v1/verify-email/**",
        "/api/v1/forgot-password",
        "/api/v1/reset-password/**",
        "/images/**",
        "/uploads/**"
    )

    const val EMAIL_NOT_FOUND = "Email address does not exist."
    const val EMAIL_EXISTS = "User exists with this email address."
    const val SAME_EMAIL = "You cannot update with your existing email."
    const val USER_NOT_FOUND = "No user found."
    const val POST_NOT_FOUND = "No post found."
    const val TAG_NOT_FOUND = "No post found."
    const val TAG_EXISTS = "Tag already exists."
    const val COUNTRY_NOT_FOUND = "No post found."
    const val COUNTRY_EXISTS = "Country already exists."
    const val EMPTY_POST = "Post does not contain any content"
    const val SHARE_NOT_FOUND = "No share found."
    const val SHARE_EXISTS = "You have already shared this post."
    const val DUPLICATE_SHARE = "You have already shared this post."
    const val NOTIFICATION_NOT_FOUND = "No notification found."

    const val EMAIL_PATTERN =
        "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"

    const val PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,32}$"
}
