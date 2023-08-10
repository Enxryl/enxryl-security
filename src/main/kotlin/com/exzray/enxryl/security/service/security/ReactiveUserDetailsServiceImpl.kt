package com.exzray.enxryl.security.service.security

import com.exzray.enxryl.security.domain.AccountDocument
import com.exzray.enxryl.security.domain.AccountRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class ReactiveUserDetailsServiceImpl(private val accountRepository: AccountRepository): ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return accountRepository
            .findAccountDocumentByUsername(username)
            .filter(Objects::nonNull)
            .switchIfEmpty(Mono.error(BadCredentialsException(String.format("User %s not found in database", username))))
            .map { createUser(it) }
    }

    fun createUser(account: AccountDocument): User{
        val authorities: List<GrantedAuthority> = mutableListOf()
        return User(account.username, account.password, authorities)
    }
}