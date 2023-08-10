package com.exzray.enxryl.security.service.security

import com.exzray.enxryl.security.dto.ResponseCommon
import com.exzray.enxryl.security.dto.authentication.ReqLogin
import com.exzray.enxryl.security.dto.authentication.ResLogin
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

interface AuthenticationService {
    fun login(body: ReqLogin): Mono<ResponseEntity<ResponseCommon<ResLogin>>>
}