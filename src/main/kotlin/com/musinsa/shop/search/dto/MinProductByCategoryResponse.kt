package com.musinsa.shop.search.dto

import com.musinsa.shop.product.Product
import com.musinsa.shop.util.toPriceString

data class MinProductByCategoryResponse(
    val list: List<MinProductByCategory>,
    val total: String
) {
    companion object {
        fun of(list: List<Product>): MinProductByCategoryResponse {
            return MinProductByCategoryResponse(
                list = list.map { MinProductByCategory.of(it) },
                total = list.sumOf { it.price }.toPriceString()
            )
        }
    }
}

data class MinProductByCategory(val category: String, val brandName: String, val price: String) {
    companion object {
        fun of(product: Product): MinProductByCategory {
            return MinProductByCategory(
                category = product.category.description,
                brandName = product.brand.name,
                price = product.price.toPriceString()
            )
        }
    }
}