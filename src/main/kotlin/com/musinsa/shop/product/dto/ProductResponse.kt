package com.musinsa.shop.product.dto

import com.musinsa.shop.product.Product
import com.musinsa.shop.product.ProductCategory

data class ProductResponse(
    val id: Long,
    val category: ProductCategory,
    val price: Int
) {
    companion object {
        fun of(product: Product) = ProductResponse(product.id!!, product.category, product.price)
    }
}