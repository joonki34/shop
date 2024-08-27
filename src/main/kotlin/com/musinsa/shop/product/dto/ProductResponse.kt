package com.musinsa.shop.product.dto

import com.musinsa.shop.product.Product

data class ProductResponse(
    val id: Long,
    val category: String,
    val price: Int
) {
    companion object {
        fun of(product: Product) = ProductResponse(product.id!!, product.category.description, product.price)
    }
}