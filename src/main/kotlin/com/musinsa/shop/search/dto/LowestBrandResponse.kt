package com.musinsa.shop.search.dto

import com.musinsa.shop.product.dto.ProductResponse

data class LowestBrandResponse(
    val lowest: LowestBrand
)

data class LowestBrand(
    val brandName: String,
    val categoryList: List<ProductResponse>,
    val total: String
)
