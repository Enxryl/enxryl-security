package com.exzray.enxryl.security.service.token

import dev.paseto.jpaseto.Claims
import dev.paseto.jpaseto.Paseto
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.util.function.Function

interface TokenProviderService {
    fun extractUsername(token: String): String

    fun extractExpiration(token: String): Instant

    fun isTokenExpired(token: String): Boolean

    fun getClaims(token: String): Claims

    fun <T> extractClaims(token: String, claimsResolver: Function<Claims, T>): T

    fun generateToken(user: UserDetails): String

    fun parseToken(token: String): Paseto

    fun validateToken(token: String): Boolean
}