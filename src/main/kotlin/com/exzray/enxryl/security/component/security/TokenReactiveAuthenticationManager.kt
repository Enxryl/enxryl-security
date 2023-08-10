package com.exzray.enxryl.security.component.security

import com.exzray.enxryl.security.service.security.ReactiveUserDetailsServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class TokenReactiveAuthenticationManager(private val userDetailsService: ReactiveUserDetailsService, private val passwordEncoder: PasswordEncoder) :
    ReactiveAuthenticationManager {
    private val logger = LoggerFactory.getLogger(TokenReactiveAuthenticationManager::class.java)

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        if (authentication!!.isAuthenticated) {
            return Mono.just(authentication)
        }

        return Mono
            .just(authentication)
            .switchIfEmpty(Mono.defer { raiseBadCredentials<Authentication>() })
            .cast(UsernamePasswordAuthenticationToken::class.java)
            .flatMap {authenticateToken(it) }
            .publishOn(Schedulers.parallel())
            .onErrorResume { raiseBadCredentials() }
            .filter { passwordEncoder.matches(authentication.credentials.toString(), it.password) }
            .switchIfEmpty(Mono.defer { raiseBadCredentials<UserDetails>() })
            .map { UsernamePasswordAuthenticationToken(authentication.principal, authentication.credentials, it.authorities) }
    }

    private fun <T> raiseBadCredentials(): Mono<T> {
        return Mono.error(BadCredentialsException("Invalid Credentials"))
    }

    private fun authenticateToken(authenticationToken: UsernamePasswordAuthenticationToken): Mono<UserDetails>? {
        val username = authenticationToken.getName()

        logger.info("checking authentication for user $username")
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            logger.info("authenticated user $username, setting security context")
            return this.userDetailsService.findByUsername(username)
        }

        return null
    }
}