package com.exzray.enxryl.security.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("accounts")
class AccountDocument {
    @Id
    lateinit var id: String

    var username: String = ""

    var password: String = ""

    var firstname: String = ""

    var lastname: String = ""

    var accountType: String = ""

    var email: String = ""

    var isActive: Boolean = true

    var dateJoined: LocalDate = LocalDate.now()
}