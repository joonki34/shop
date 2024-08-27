package com.musinsa.shop.product.dto

import com.musinsa.shop.product.Product
import com.musinsa.shop.util.toPriceString

data class ProductResponse(
    val id: Long,
    val category: String,
    val price: String
) {
    companion object {
        fun of(product: Product) =
            ProductResponse(product.id!!, product.category.description, product.price.toPriceString())
    }
}