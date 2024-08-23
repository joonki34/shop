package com.musinsa.shop.product.dto

import com.musinsa.shop.brand.Brand
import com.musinsa.shop.product.Product
import com.musinsa.shop.product.ProductCategory
import jakarta.validation.constraints.Min

data class ProductCreateRequest(
    val brandId: Long,
    val category: ProductCategory,
    @field:Min(0)
    val price: Int
) {
    fun toEntity(brand: Brand) = Product(brand = brand, category = category, price = price)
}
