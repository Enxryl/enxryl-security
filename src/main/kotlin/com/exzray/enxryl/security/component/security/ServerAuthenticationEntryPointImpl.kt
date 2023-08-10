package com.exzray.enxryl.security.component.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class ServerAuthenticationEntryPointImpl : ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange?, ex: AuthenticationException?): Mono<Void> {
        return Mono.fromRunnable { exchange!!.response.setStatusCode(HttpStatus.UNAUTHORIZED) }
    }
}