package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.User

interface UserService {
    fun getUserById(userId: Long): User
    fun getUserByEmail(email: String): User
    fun createNewUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): User

    fun getAuthenticatedUser(): User
}
