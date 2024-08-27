package com.musinsa.shop.fixtures

import com.musinsa.shop.brand.Brand
import com.musinsa.shop.product.Product
import com.musinsa.shop.product.ProductCategory

object Fixtures {
    val product = Product(
        id = 1L,
        brand = Brand(id = 1L, name = "test"),
        price = 10000,
        category = ProductCategory.BAG
    )
}