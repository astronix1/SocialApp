package com.astronix1.socialapp.filter

import com.astronix1.socialapp.common.AppConstants
import com.astronix1.socialapp.service.JwtTokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthorizationFilter(
    private val jwtTokenService: JwtTokenService
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (request.method.equals(AppConstants.OPTIONS_HTTP_METHOD, ignoreCase = true)) {
            response.status = HttpStatus.OK.value()
            filterChain.doFilter(request, response)
            return
        }

        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorizationHeader.isNullOrBlank() ||
            !authorizationHeader.startsWith(AppConstants.TOKEN_PREFIX)
        ) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authorizationHeader.substring(AppConstants.TOKEN_PREFIX.length)
        val email = jwtTokenService.getSubjectFromToken(token)

        if (jwtTokenService.isTokenValid(email, token) &&
            SecurityContextHolder.getContext().authentication == null
        ) {
            val authorities: List<GrantedAuthority> =
                jwtTokenService.getAuthoritiesFromToken(token)

            val authentication = UsernamePasswordAuthenticationToken(
                email,
                null,
                authorities
            )

            authentication.details =
                WebAuthenticationDetailsSource().buildDetails(request)

            SecurityContextHolder.getContext().authentication = authentication
        } else {
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}
