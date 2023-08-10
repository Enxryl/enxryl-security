package com.exzray.enxryl.security.dto.account

import java.time.LocalDate

data class AccountDTO(
    val username: String,
    val firstname: String,
    val lastname: String,
    val accountType: String,
    val email: String,
    val isActive: Boolean,
    val dateJoined: LocalDate
)
