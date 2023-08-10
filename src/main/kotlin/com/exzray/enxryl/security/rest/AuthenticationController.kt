package com.exzray.enxryl.security.rest

import com.exzray.enxryl.security.dto.authentication.ReqLogin
import com.exzray.enxryl.security.dto.ResponseCommon
import com.exzray.enxryl.security.dto.authentication.ResLogin
import com.exzray.enxryl.security.service.security.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/authentication")
class AuthenticationController(private val authenticationService: AuthenticationService) {
    @PostMapping("/login")
    fun login(@RequestBody body: ReqLogin): Mono<ResponseEntity<ResponseCommon<ResLogin>>> {
        return authenticationService.login(body)
    }

    @PostMapping("/validate")
    fun validate(): Mono<ResponseEntity<ResponseCommon<Any>>> {
        return Mono.just(ResponseEntity.ok().build())
    }

    @PostMapping("/logout")
    fun logout(): Mono<ResponseEntity<ResponseCommon<Any>>> {
        return Mono.just(ResponseEntity.ok().build())
    }
}