package com.exzray.enxryl.security.component.security

import com.exzray.enxryl.security.service.token.TokenProviderService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.function.Function
import java.util.function.Predicate

class TokenAuthenticationConverter(
    private val reactiveUserDetailsService: ReactiveUserDetailsService,
    private val tokenProviderService: TokenProviderService
) :
    Function<ServerWebExchange, Mono<Authentication>> {
    companion object {
        private const val BEARER = "Bearer "
        private val tokenCheck: Predicate<String> = Predicate { it.length > BEARER.length }
        private val tokenValue: Function<String, String> = Function { it.substring(BEARER.length) }
    }

    override fun apply(exchange: ServerWebExchange): Mono<Authentication> {
        return Mono
            .justOrEmpty(exchange)
            .map { SecurityUtils.getTokenFromRequest(it) }
            .filter { it.isNotEmpty() }
            .filter(tokenCheck)
            .map(tokenValue)
            .filter { it.isNotEmpty() }
            .map { tokenProviderService.extractUsername(it) }
            .flatMap { reactiveUserDetailsService.findByUsername(it) }
            .map { UsernamePasswordAuthenticationToken(it, SecurityUtils.getTokenFromRequest(exchange), it.authorities) }
    }
}