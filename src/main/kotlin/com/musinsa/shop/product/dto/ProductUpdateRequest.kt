package com.musinsa.shop.product.dto

import jakarta.validation.constraints.Min

data class ProductUpdateRequest(
    val category: String,
    @field:Min(0)
    val price: Int
)
