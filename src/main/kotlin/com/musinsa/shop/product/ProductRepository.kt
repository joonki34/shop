package com.musinsa.shop.product

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByBrandId(brandId: Long): List<Product>

    fun findByBrandIdAndCategory(brandId: Long, category: ProductCategory): Product?

    @EntityGraph(attributePaths = ["brand"])
    fun findFirstByCategoryOrderByPrice(category: ProductCategory): Product?

    @EntityGraph(attributePaths = ["brand"])
    fun findFirstByCategoryOrderByPriceDesc(category: ProductCategory): Product?
}
