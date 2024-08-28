package com.musinsa.shop.minproduct

import com.musinsa.shop.common.BaseAuditEntity
import com.musinsa.shop.product.ProductCategory
import jakarta.persistence.*

@Entity
@Table(name = "min_product")
class MinProduct(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "category", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    val category: ProductCategory,

    @Column(name = "product_id", nullable = false)
    var productId: Long,
) : BaseAuditEntity() {
    fun updateProductId(productId: Long): MinProduct {
        this.productId = productId
        return this
    }
}