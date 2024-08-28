package com.musinsa.shop.product

import com.musinsa.shop.brand.Brand
import com.musinsa.shop.brand.BrandService
import com.musinsa.shop.exception.NotFound
import com.musinsa.shop.product.dto.ProductCreateRequest
import com.musinsa.shop.product.dto.ProductUpdateRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val repository: ProductRepository,
    private val brandService: BrandService,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun getProductList(brandName: String?): List<Product> {
        return if (brandName == null) repository.findAll()
        else repository.findByBrandName(brandName)
    }

    fun getProduct(id: Long): Product {
        return repository.findById(id).orElseThrow { NotFound("Product not found") }
    }

    @Transactional
    fun createProduct(request: ProductCreateRequest): Product {
        val brand = brandService.getBrand(request.brandId)
        val category =
            ProductCategory.descriptionMap[request.category] ?: throw IllegalArgumentException("Invalid request")

        // Only one product per category is allowed
        val productByCategory = repository.findByBrandIdAndCategory(brand.id!!, category)
        if (productByCategory != null) {
            throw IllegalArgumentException("Only one product per category is allowed")
        }

        // Update brand price
        updateBrandPrice(brand, request.price)


        return repository.save(request.toEntity(brand)).also {
            // Publish event
            eventPublisher.publishEvent(ProductEvent.of(this, ProductEventType.CREATED, it))
        }
    }

    @Transactional
    fun updateProduct(id: Long, request: ProductUpdateRequest): Product {
        val product = repository.findById(id).orElseThrow { NotFound("Product not found") }
        val category =
            ProductCategory.descriptionMap[request.category] ?: throw IllegalArgumentException("Invalid request")

        // Only one product per category is allowed
        val productByCategory = repository.findByBrandIdAndCategory(product.brand.id!!, category)
        if (productByCategory != null && product.id != productByCategory.id) {
            throw IllegalArgumentException("Only one product per category is allowed")
        }

        // Update brand price
        updateBrandPrice(product.brand, request.price - product.price)

        return repository.save(product.update(category, request.price)).also {
            // Publish event
            eventPublisher.publishEvent(ProductEvent.of(this, ProductEventType.UPDATED, it))
        }
    }

    @Transactional
    fun deleteProduct(id: Long) {
        val product = repository.findById(id).orElseThrow { NotFound("Product not found") }

        repository.delete(product)

        // Publish event
        eventPublisher.publishEvent(ProductEvent.of(this, ProductEventType.DELETED, product))

        // Update brand price
        updateBrandPrice(product.brand, -product.price)
    }

    private fun updateBrandPrice(brand: Brand, price: Int) {
        brandService.updatePrice(brand, price)
    }

}