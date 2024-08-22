package com.musinsa.shop.brand.dto

import com.musinsa.shop.brand.Brand

data class BrandCreateRequest(val name: String) {
    fun toEntity() = Brand(name = name)
}
