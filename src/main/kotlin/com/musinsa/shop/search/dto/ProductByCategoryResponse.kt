package com.musinsa.shop.search.dto

import com.musinsa.shop.product.Product
import com.musinsa.shop.util.toPriceString

data class ProductByCategoryResponse(
    val category: String,
    val min: ProductByCategoryDetail?,
    val max: ProductByCategoryDetail?
)

data class ProductByCategoryDetail(
    val brandName: String,
    val price: String
) {
    companion object {
        fun of(product: Product): ProductByCategoryDetail {
            return ProductByCategoryDetail(product.brand.name, product.price.toPriceString())
        }
    }
}