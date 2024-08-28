package com.musinsa.shop.brand.dto

import com.musinsa.shop.brand.Brand
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class BrandCreateRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^[A-Za-z0-9]*$")
    val name: String
) {
    fun toEntity() = Brand(name = name, totalPrice = 0)
}
