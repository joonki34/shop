package com.musinsa.shop.minproduct

import com.musinsa.shop.product.Product
import com.musinsa.shop.product.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MinProductRepository : JpaRepository<MinProduct, Long> {
    fun findByCategory(category: ProductCategory): MinProduct?

    @Query(
        """
        select p
        from MinProduct m INNER JOIN Product p ON m.productId = p.id
    """
    )
    fun findAllProduct(): List<Product>
}