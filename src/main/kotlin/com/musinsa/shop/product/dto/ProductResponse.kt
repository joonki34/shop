package com.musinsa.shop.product.dto

import com.musinsa.shop.product.Product
import com.musinsa.shop.util.toPriceString

data class ProductResponse(
    val id: Long,
    val brandId: Long,
    val brandName: String,
    val category: String,
    val price: String
) {
    companion object {
        fun of(product: Product) =
            ProductResponse(
                id = product.id!!,
                brandId = product.brand.id!!,
                brandName = product.brand.name,
                category = product.category.description,
                price = product.price.toPriceString()
            )
    }
}