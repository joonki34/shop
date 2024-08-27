package com.musinsa.shop.brand

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface BrandRepository: JpaRepository<Brand, Long> {
    @Modifying
    @Query("UPDATE Brand b SET b.totalPrice = b.totalPrice + :price WHERE b.id = :id")
    fun updateTotalPrice(id: Long, price: Int)

    fun findFirstByOrderByTotalPrice(): Brand?
}