package com.exzray.enxryl.security.component.security

import org.apache.logging.log4j.util.Strings
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.User
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.security.Principal

class SecurityUtils {
    companion object {
        fun getTokenFromRequest(exchange: ServerWebExchange): String {
            val token = exchange
                .request
                .headers
                .getFirst(HttpHeaders.AUTHORIZATION)

            return if (token.isNullOrEmpty()) Strings.EMPTY else token
        }

        fun getUserFromRequest(exchange: ServerWebExchange): Mono<String> {
            return exchange
                .getPrincipal<Principal>()
                .cast(UsernamePasswordAuthenticationToken::class.java)
                .map { it.principal }
                .cast(User::class.java)
                .map { it.username }
        }
    }
}