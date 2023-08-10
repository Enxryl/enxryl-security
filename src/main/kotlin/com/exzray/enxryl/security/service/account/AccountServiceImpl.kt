package com.exzray.enxryl.security.service.account

import com.exzray.enxryl.security.component.custom.EnxrylException
import com.exzray.enxryl.security.domain.AccountDocument
import com.exzray.enxryl.security.domain.AccountRepository
import com.exzray.enxryl.security.dto.account.AccountDTO
import com.exzray.enxryl.security.dto.account.ReqCreateAccount
import com.exzray.enxryl.security.dto.ResponseCommon
import com.exzray.enxryl.security.dto.account.ReqUpdateAccount
import com.exzray.enxryl.security.dto.account.ReqUpdateAccountPassword
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDate

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) : AccountService {
    override fun createAccountMerchant(body: ReqCreateAccount): Mono<ResponseEntity<ResponseCommon<AccountDTO>>> {
        val account: AccountDocument = createAccount(body, "MERCHANT")
        return saveAccount(account)
    }

    override fun createAccountUser(body: ReqCreateAccount): Mono<ResponseEntity<ResponseCommon<AccountDTO>>> {
        val account: AccountDocument = createAccount(body, "USER")
        return saveAccount(account)
    }

    override fun updateAccountPassword(
        accountID: String,
        body: ReqUpdateAccountPassword
    ): Mono<ResponseEntity<ResponseCommon<AccountDTO>>> {
        return accountRepository
            .findById(accountID)
            .switchIfEmpty { Mono.error(EnxrylException("Account not found!")) }
            .filter { passwordEncoder.matches(body.oldPassword, it.password) }
            .switchIfEmpty { Mono.error(EnxrylException("Password wrong!")) }
            .flatMap {
                val updatedAccount = it.apply {
                    password = passwordEncoder.encode(body.newPassword)
                }
                accountRepository.save(updatedAccount)
            }
            .map {
                ResponseEntity.status(HttpStatus.OK).body(ResponseCommon(toDTO(it), ""))
            }
    }

    override fun updateAccount(
        accountID: String,
        body: ReqUpdateAccount
    ): Mono<ResponseEntity<ResponseCommon<AccountDTO>>> {
        return accountRepository
            .findById(accountID)
            .switchIfEmpty { Mono.error(EnxrylException("Account not found!")) }
            .flatMap {
                val updatedAccount = it.apply {
                    if (body.firstname != it.firstname) {
                        it.firstname = body.firstname
                    }

                    if (body.lastname != it.lastname) {
                        it.lastname = body.lastname
                    }
                }
                accountRepository.save(updatedAccount)
            }
            .map {
                ResponseEntity.status(HttpStatus.OK).body(ResponseCommon(toDTO(it), ""))
            }
    }

    override fun deleteAccount(accountID: String): Mono<ResponseEntity<ResponseCommon<Any>>> {
        TODO("Not yet implemented")
    }

    override fun reactivateAccount(accountID: String): Mono<ResponseEntity<ResponseCommon<AccountDTO>>> {
        return accountRepository
            .findById(accountID)
            .switchIfEmpty { Mono.error(EnxrylException("Account not found!")) }
            .flatMap {
                val updatedAccount = it.apply {
                    it.isActive = true
                }
                accountRepository.save(updatedAccount)
            }
            .map {
                ResponseEntity.status(HttpStatus.OK).body(ResponseCommon(toDTO(it), ""))
            }
    }

    private fun createAccount(body: ReqCreateAccount, accountType: String): AccountDocument {
        return AccountDocument()
            .also {
                it.username = body.username
                it.password = passwordEncoder.encode(body.password)
                it.firstname = body.firstname
                it.lastname = body.lastname
                it.email = body.email
                it.accountType = accountType
            }
    }

    private fun saveAccount(account: AccountDocument): Mono<ResponseEntity<ResponseCommon<AccountDTO>>> {
        return accountRepository
            .existsAccountDocumentByUsernameOrEmail(account.username, account.password)
            .flatMap {
                if (it) {
                    Mono.error(EnxrylException("Account ${account.username} already exists!"))
                } else {
                    accountRepository.save(account)
                }
            }
            .map {
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ResponseCommon(toDTO(it), ""))
            }
    }

    private fun toDTO(document: AccountDocument): AccountDTO {
        return AccountDTO(
            document.accountType,
            document.lastname,
            document.username,
            document.firstname,
            document.email,
            document.isActive,
            document.dateJoined
        )
    }
}