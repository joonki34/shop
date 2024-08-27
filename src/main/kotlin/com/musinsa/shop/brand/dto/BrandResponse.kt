package com.musinsa.shop.brand.dto

import com.musinsa.shop.brand.Brand

data class BrandResponse(val id: Long, val name: String) {
    companion object {
        fun of(brand: Brand) = BrandResponse(brand.id!!, brand.name)
    }
}
