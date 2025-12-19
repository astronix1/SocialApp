package com.astronix1.socialapp.service

import com.astronix1.socialapp.common.UserPrincipal
import com.astronix1.socialapp.exception.UserNotFoundException
import com.astronix1.socialapp.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UserNotFoundException("No user exists with this email") }

        return UserPrincipal(user)
    }
}
