package com.exzray.enxryl.security.component.security

import org.springframework.http.HttpHeaders
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

class TokenHeadersExchangeMatcher : ServerWebExchangeMatcher {
    override fun matches(exchange: ServerWebExchange): Mono<ServerWebExchangeMatcher.MatchResult> {
        val request = Mono
            .just(exchange)
            .map { it.request }

        return request
            .map { it.headers }
            .filter { it.containsKey(HttpHeaders.AUTHORIZATION) }
            .flatMap { ServerWebExchangeMatcher.MatchResult.match() }
            .switchIfEmpty { ServerWebExchangeMatcher.MatchResult.notMatch() }
    }
}