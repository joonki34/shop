package com.musinsa.shop.search.dto

data class MinProductByCategoryResponse(
    val list: List<MinProduct>,
    val total: String
)

data class MinProduct(val category: String, val brandName: String, val price: String)