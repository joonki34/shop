package com.musinsa.shop.product.dto

import com.musinsa.shop.product.ProductCategory
import jakarta.validation.constraints.Min

data class ProductUpdateRequest(
    val category: ProductCategory,
    @field:Min(0)
    val price: Int
)
