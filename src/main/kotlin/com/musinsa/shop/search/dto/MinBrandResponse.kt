package com.musinsa.shop.search.dto

import com.musinsa.shop.brand.Brand
import com.musinsa.shop.product.Product
import com.musinsa.shop.util.toPriceString

data class MinBrandResponse(
    val min: MinBrand
) {
    companion object {
        fun of(brand: Brand, list: List<Product>): MinBrandResponse {
            return MinBrandResponse(
                min = MinBrand(
                    brandName = brand.name,
                    categoryList = list.map { MinBrandProductResponse.of(it) },
                    total = brand.totalPrice.toPriceString()
                )
            )
        }
    }
}

data class MinBrand(
    val brandName: String,
    val categoryList: List<MinBrandProductResponse>,
    val total: String
)

data class MinBrandProductResponse(
    val category: String,
    val price: String
) {
    companion object {
        fun of(product: Product): MinBrandProductResponse {
            return MinBrandProductResponse(
                category = product.category.description,
                price = product.price.toPriceString()
            )
        }
    }
}
