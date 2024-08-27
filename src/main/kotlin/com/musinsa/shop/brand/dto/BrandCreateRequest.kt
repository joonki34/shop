package com.musinsa.shop.brand.dto

import com.musinsa.shop.brand.Brand
import jakarta.validation.constraints.NotBlank

data class BrandCreateRequest(@field:NotBlank val name: String) {
    fun toEntity() = Brand(name = name, totalPrice = 0)
}
