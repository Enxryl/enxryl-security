package com.exzray.enxryl.security.dto.account

data class ReqCreateAccount(
    val username: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val email: String
)
