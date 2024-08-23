package com.musinsa.shop.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByBrandId(brandId: Long): List<Product>

    fun findByBrandIdAndCategory(brandId: Long, category: ProductCategory): Product?
}
