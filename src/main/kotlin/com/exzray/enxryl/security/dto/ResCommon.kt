package com.exzray.enxryl.security.dto

data class ResponseCommon <T> (private val data: T, private val message: String) : IResponseCommon<T> {
    override fun getData(): T {
        return data
    }

    override fun getMessage(): String {
        return message
    }
}

sealed interface IResponseCommon<T> {
    fun getData(): T

    fun getMessage(): String
}