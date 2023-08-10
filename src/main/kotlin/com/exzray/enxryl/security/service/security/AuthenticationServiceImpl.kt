package com.exzray.enxryl.security.service.security

import com.exzray.enxryl.security.component.custom.EnxrylException
import com.exzray.enxryl.security.dto.ResponseCommon
import com.exzray.enxryl.security.dto.authentication.ReqLogin
import com.exzray.enxryl.security.dto.authentication.ResLogin
import com.exzray.enxryl.security.service.token.TokenProviderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class AuthenticationServiceImpl(
    private val userDetailsService: ReactiveUserDetailsService,
    private val tokenProviderService: TokenProviderService,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationService {
    override fun login(body: ReqLogin): Mono<ResponseEntity<ResponseCommon<ResLogin>>> {
        return userDetailsService
            .findByUsername(body.username)
            .switchIfEmpty { Mono.error(EnxrylException("Account ${body.username}} not exists!")) }
            .filter { passwordEncoder.matches(body.password, it.password) }
            .switchIfEmpty { Mono.error(EnxrylException("Password wrong!")) }
            .map {
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseCommon(ResLogin(tokenProviderService.generateToken(it)), ""))
            }
    }
}