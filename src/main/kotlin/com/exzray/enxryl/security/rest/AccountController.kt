package com.exzray.enxryl.security.rest

import com.exzray.enxryl.security.dto.account.AccountDTO
import com.exzray.enxryl.security.dto.account.ReqCreateAccount
import com.exzray.enxryl.security.dto.ResponseCommon
import com.exzray.enxryl.security.service.account.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/account")
class AccountController(private val accountService: AccountService) {
    @PostMapping("/register-merchant")
    fun registerAsMerchant(@RequestBody body: ReqCreateAccount): Mono<ResponseEntity<ResponseCommon<AccountDTO>>> {
        return accountService.createAccountMerchant(body)
    }

    @PostMapping("/register-user")
    fun registerAsUser(@RequestBody body: ReqCreateAccount): Mono<ResponseEntity<ResponseCommon<AccountDTO>>> {
        return accountService.createAccountUser(body)
    }
}