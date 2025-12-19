package com.astronix1.socialapp.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.astronix1.socialapp.common.AppConstants
import com.astronix1.socialapp.common.UserPrincipal
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtTokenService(
    @Value("\${jwt.secret}")
    private val jwtSecret: String
) {

    /* ================== TOKEN GENERATION ================== */

    fun generateToken(userPrincipal: UserPrincipal): String {
        val claims = getClaimsFromUser(userPrincipal)

        return JWT.create()
            .withSubject(userPrincipal.username)
            .withArrayClaim(AppConstants.AUTHORITIES, claims)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + AppConstants.JWT_EXPIRATION_2Wk))
            .sign(Algorithm.HMAC512(jwtSecret.toByteArray()))
    }

    /* ================== TOKEN PARSING ================== */

    fun getAuthoritiesFromToken(token: String): List<GrantedAuthority> {
        val claims = getClaimsFromToken(token)
        return claims.map { SimpleGrantedAuthority(it) }
    }

    fun getSubjectFromToken(token: String): String {
        return getJwtVerifier().verify(token).subject
    }

    fun isTokenValid(email: String, token: String): Boolean {
        return email.isNotBlank() && !isTokenExpired(token)
    }


    /* ================== INTERNAL HELPERS ================== */

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getJwtVerifier().verify(token).expiresAt
        return expiration.before(Date())
    }

    private fun getClaimsFromUser(userPrincipal: UserPrincipal): Array<String> {
        return userPrincipal.authorities
            .map { it.authority }
            .toTypedArray()
    }

    private fun getClaimsFromToken(token: String): Array<String> {
        return getJwtVerifier()
            .verify(token)
            .getClaim(AppConstants.AUTHORITIES)
            .asArray(String::class.java)
    }

    private fun getJwtVerifier(): JWTVerifier {
        return try {
            val algorithm = Algorithm.HMAC512(jwtSecret.toByteArray())
            JWT.require(algorithm).build()
        } catch (e: JWTVerificationException) {
            throw JWTVerificationException(AppConstants.TOKEN_UNVERIFIABLE)
        }
    }
}
