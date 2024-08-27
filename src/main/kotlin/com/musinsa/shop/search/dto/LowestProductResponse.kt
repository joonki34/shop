package com.musinsa.shop.search.dto

data class LowestProductByCategoryResponse(
    val list: List<LowestProduct>,
    val total: String
)

data class LowestProduct(val category: String, val brandName: String, val price: String)