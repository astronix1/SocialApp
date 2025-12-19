package com.astronix1.socialapp.response

import com.astronix1.socialapp.entity.User

data class UserResponse(
    val user: User,
    val followedByAuthUser: Boolean
)
