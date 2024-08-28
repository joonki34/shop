package com.musinsa.shop.fixtures

import com.musinsa.shop.brand.Brand
import com.musinsa.shop.minproduct.MinProduct
import com.musinsa.shop.product.Product
import com.musinsa.shop.product.ProductCategory
import com.musinsa.shop.product.ProductEvent
import com.musinsa.shop.product.ProductEventType

object Fixtures {
    fun createBrand(totalPrice: Int = 0) =
        Brand(id = 1L, name = "Test Brand", totalPrice = totalPrice)

    val brand = Brand(id = 1L, name = "Test Brand", totalPrice = 0)

    fun createProduct(id: Long = 1L, category: ProductCategory = ProductCategory.BAG, price: Int = 10000) =
        Product(id = id, brand = brand, price = price, category = category)

    val product = createProduct()

    fun createProductEvent(eventType: ProductEventType = ProductEventType.CREATED, product: Product = createProduct()) =
        ProductEvent.of(1, eventType, product)

    fun createMinProduct(category: ProductCategory = ProductCategory.BAG, productId: Long = product.id!!) =
        MinProduct(1L, category, productId)
}