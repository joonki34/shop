package com.musinsa.shop.minproduct

import com.musinsa.shop.common.BaseAuditEntity
import com.musinsa.shop.product.Product
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product,
) : BaseAuditEntity() {
    fun updateProduct(product: Product): MinProduct {
        if (product.price <= this.product.price) {
            this.product = product
        }
        return this
    }
}