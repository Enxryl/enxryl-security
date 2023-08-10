package com.exzray.enxryl.security.service.account

import com.exzray.enxryl.security.dto.account.AccountDTO
import com.exzray.enxryl.security.dto.account.ReqCreateAccount
import com.exzray.enxryl.security.dto.ResponseCommon
import com.exzray.enxryl.security.dto.account.ReqUpdateAccount
import com.exzray.enxryl.security.dto.account.ReqUpdateAccountPassword
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

interface AccountService {
    fun createAccountMerchant(body: ReqCreateAccount): Mono<ResponseEntity<ResponseCommon<AccountDTO>>>

    fun createAccountUser(body: ReqCreateAccount): Mono<ResponseEntity<ResponseCommon<AccountDTO>>>

    fun updateAccountPassword(accountID: String, body: ReqUpdateAccountPassword): Mono<ResponseEntity<ResponseCommon<AccountDTO>>>

    fun updateAccount(accountID: String, body: ReqUpdateAccount): Mono<ResponseEntity<ResponseCommon<AccountDTO>>>

    fun deleteAccount(accountID: String): Mono<ResponseEntity<ResponseCommon<Any>>>

    fun reactivateAccount(accountID: String): Mono<ResponseEntity<ResponseCommon<AccountDTO>>>
}