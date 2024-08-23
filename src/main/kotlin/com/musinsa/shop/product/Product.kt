package com.musinsa.shop.product

import com.musinsa.shop.brand.Brand
import com.musinsa.shop.common.BaseAuditEntity
import jakarta.persistence.*

@Entity
@Table(name = "product")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: Brand,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    var category: ProductCategory,

    @Column(name = "price", nullable = false)
    var price: Int,
) : BaseAuditEntity() {
    fun update(category: ProductCategory, price: Int): Product {
        this.category = category
        this.price = price
        return this
    }
}