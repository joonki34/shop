package com.musinsa.shop.product

import com.musinsa.shop.brand.BrandService
import com.musinsa.shop.exception.NotFound
import com.musinsa.shop.product.dto.ProductCreateRequest
import com.musinsa.shop.product.dto.ProductUpdateRequest
import org.springframework.stereotype.Service

@Service
class ProductService(private val repository: ProductRepository, private val brandService: BrandService) {
    fun getProduct(id: Long): Product {
        return repository.findById(id).orElseThrow { NotFound("Product not found") }
    }

    fun createProduct(request: ProductCreateRequest): Product {
        val brand = brandService.getBrand(request.brandId)

        // Only one product per category is allowed
        val productByCategory = repository.findByBrandIdAndCategory(brand.id!!, request.category)
        if (productByCategory != null) {
            throw IllegalArgumentException("Only one product per category is allowed")
        }

        return repository.save(request.toEntity(brand))
    }

    fun updateProduct(id: Long, request: ProductUpdateRequest): Product {
        val product = repository.findById(id).orElseThrow { NotFound("Product not found") }

        // Only one product per category is allowed
        val productByCategory = repository.findByBrandIdAndCategory(product.brand.id!!, request.category)
        if (productByCategory != null && product.id != productByCategory.id) {
            throw IllegalArgumentException("Only one product per category is allowed")
        }

        return repository.save(product.update(request.category, request.price))
    }

    fun deleteProduct(id: Long) {
        repository.deleteById(id)
    }
}