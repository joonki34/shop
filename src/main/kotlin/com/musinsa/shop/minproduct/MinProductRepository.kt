package com.musinsa.shop.minproduct

import com.musinsa.shop.product.ProductCategory
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface MinProductRepository : JpaRepository<MinProduct, Long> {
    @EntityGraph(attributePaths = ["product"])
    fun findByCategory(category: ProductCategory): MinProduct?

    @EntityGraph(attributePaths = ["product"])
    override fun findAll(): MutableList<MinProduct>
}