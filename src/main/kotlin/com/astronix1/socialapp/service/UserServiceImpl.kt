package com.astronix1.socialapp.service

import com.astronix1.socialapp.entity.User
import com.astronix1.socialapp.exception.UserNotFoundException
import com.astronix1.socialapp.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun getUserById(userId: Long): User =
        userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

    override fun getUserByEmail(email: String): User =
        userRepository.findByEmail(email)
            .orElseThrow { UserNotFoundException() }

    override fun createNewUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): User {
        if (userRepository.findByEmail(email).isPresent) {
            throw IllegalStateException("Email already exists")
        }

        val user = User(
            email = email,
            password = passwordEncoder.encode(password),
            firstName = firstName,
            lastName = lastName,
            role = "ROLE_USER",
            enabled = true,
            accountVerified = false,
            emailVerified = false,
            followerCount = 0,
            followingCount = 0,
            joinDate = LocalDateTime.now(),
            dateLastModified = LocalDateTime.now()
        )

        return userRepository.save(user)
    }

    override fun getAuthenticatedUser(): User {
        val email = SecurityContextHolder.getContext()
            .authentication
            .principal
            .toString()

        return getUserByEmail(email)
    }
}
