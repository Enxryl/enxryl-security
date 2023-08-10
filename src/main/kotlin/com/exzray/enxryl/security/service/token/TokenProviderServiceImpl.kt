package com.exzray.enxryl.security.service.token

import dev.paseto.jpaseto.*
import dev.paseto.jpaseto.lang.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import javax.crypto.SecretKey

@Service
class TokenProviderServiceImpl : TokenProviderService {
    private var secretKey: SecretKey = Keys.secretKey()

    private var keyPair: KeyPair = Keys.keyPairFor(Version.V2)

    override fun extractUsername(token: String): String {
        return extractClaims(token, Claims::getSubject)
    }

    override fun extractExpiration(token: String): Instant {
        return extractClaims(token, Claims::getExpiration)
    }

    override fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).isBefore(Instant.now())
    }

    override fun getClaims(token: String): Claims {
        return parseToken(token).claims
    }

    override fun <T> extractClaims(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = getClaims(token)
        return claimsResolver.apply(claims)
    }

    override fun generateToken(user: UserDetails): String {
        val now = Instant.now()
        println(secretKey.toString())
        
        val authorities = user
            .authorities
            .stream()
            .map { it.authority }
            .collect(Collectors.joining(","))

        return Pasetos.V2.LOCAL.builder()
            .setSharedSecret(secretKey)
            .setIssuedAt(now)
            .setExpiration(now.plus(2, ChronoUnit.HOURS))
            .setSubject(user.username)
            .setKeyId(UUID.randomUUID().toString())
            .setAudience("enxryl.exzray.com")
            .setIssuer("exzray.com")
            .claim("aut", authorities)
            .compact()
    }

    override fun parseToken(token: String): Paseto {
        val parser: PasetoParser = Pasetos
            .parserBuilder()
            .setSharedSecret(secretKey)
            .setPublicKey(keyPair.public)
            .build()

        return parser.parse(token)
    }

    override fun validateToken(token: String): Boolean {
        return try {
            parseToken(token)
            !isTokenExpired(token)
        } catch (e: PasetoException) {
            false
        }
    }
}