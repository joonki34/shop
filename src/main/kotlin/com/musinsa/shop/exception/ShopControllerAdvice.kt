package com.musinsa.shop.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class ShopControllerAdvice : ResponseEntityExceptionHandler() {
    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        return BadRequest(ex.message).toResponseEntity() as ResponseEntity<Any>?
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        return BadRequest(ex.message ?: "Bad Request").toResponseEntity() as ResponseEntity<Any>?
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        return BadRequest(ex.message).toResponseEntity() as ResponseEntity<Any>?
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return BadRequest(e.message ?: "Bad Request").toResponseEntity()
    }

    @ExceptionHandler(ShopException::class)
    fun handleShopException(e: ShopException): ResponseEntity<ErrorResponse> {
        log.error(e) { "${e.javaClass.simpleName}(${e.message}), Cause by: ${e.cause}" }
        return e.toResponseEntity()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error(e) { "${e.javaClass.simpleName}(${e.message}), Cause by: ${e.cause}" }
        return InternalServerError("Unknown error").toResponseEntity()
    }
}

data class ErrorResponse(val code: String, val message: String)

fun ShopException.toResponseEntity(): ResponseEntity<ErrorResponse> =
    ResponseEntity.status(httpStatus).body(ErrorResponse(code, message))