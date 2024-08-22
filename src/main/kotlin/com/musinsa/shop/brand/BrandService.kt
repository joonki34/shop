package com.musinsa.shop.brand

import com.musinsa.shop.exception.NotFound
import org.springframework.stereotype.Service

@Service
class BrandService(private val repository: BrandRepository) {
    fun createBrand(brand: Brand): Brand {
        return repository.save(brand)
    }

    fun deleteBrand(id: Long) {
        repository.deleteById(id)
    }

    fun getBrand(id: Long): Brand {
        return repository.findById(id).orElseThrow { NotFound("Brand not found") }
    }

}