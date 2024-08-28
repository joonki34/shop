package com.musinsa.shop.brand

import com.musinsa.shop.common.BaseAuditEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "brand",
    indexes = [
        Index(name = "idx_total_price", columnList = "total_price"),
    ]
)
class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "total_price", nullable = false)
    val totalPrice: Int
) : BaseAuditEntity()