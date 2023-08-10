package com.exzray.enxryl.security.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AccountRepository : ReactiveMongoRepository<AccountDocument, String> {
    fun findAccountDocumentByUsername(username: String?): Mono<AccountDocument>

    fun existsAccountDocumentByUsernameOrEmail(username: String?, email: String?): Mono<Boolean>
}