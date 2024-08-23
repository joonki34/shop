package com.musinsa.shop.exception

import org.springframework.http.HttpStatus

sealed class ShopException(val httpStatus: HttpStatus, val code: String, override val message: String) :
    RuntimeException()

data class BadRequest(override val message: String) : ShopException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message)

data class NotFound(override val message: String) : ShopException(HttpStatus.NOT_FOUND, "NOT_FOUND", message)

data class InternalServerError(override val message: String) :
    ShopException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", message)