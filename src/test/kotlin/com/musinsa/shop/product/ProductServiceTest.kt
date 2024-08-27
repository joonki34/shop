package com.musinsa.shop.product

import com.musinsa.shop.brand.BrandService
import com.musinsa.shop.exception.NotFound
import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.product.dto.ProductCreateRequest
import com.musinsa.shop.product.dto.ProductUpdateRequest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ProductServiceTest {

    private val repository = mockk<ProductRepository>()
    private val brandService = mockk<BrandService>()
    private val service = ProductService(repository, brandService)

    @BeforeEach
    fun setUp() {
        every { brandService.updatePrice(any(), any()) } just Runs
    }

    @Test
    fun `getProduct should return a product by id`() {
        // Given
        val id = 1L
        val product = Fixtures.createProduct(id)
        every { repository.findById(any()) } returns Optional.of(product)

        // When
        val foundProduct = service.getProduct(id)

        // Then
        assert(foundProduct == product)
    }

    @Test
    fun `getProduct should throw NotFound exception when product not found`() {
        // Given
        val id = 1L
        every { repository.findById(any()) } returns Optional.empty()

        // When / Then
        assertThrows<NotFound> { service.getProduct(id) }
    }

    @Test
    fun `createProduct should save and return a product`() {
        // Given
        val request = ProductCreateRequest(brandId = 1L, category = ProductCategory.TOP.description, price = 10000)
        val brand = Fixtures.brand
        val product = Fixtures.product
        every { brandService.getBrand(any()) } returns brand
        every { repository.findByBrandIdAndCategory(any(), any()) } returns null
        every { repository.save(any()) } returns product

        // When
        val createdProduct = service.createProduct(request)

        // Then
        assert(createdProduct == product)
    }

    @Test
    fun `createProduct should throw IllegalArgumentException when product with same category already exists`() {
        // Given
        val request = ProductCreateRequest(brandId = 1L, category = ProductCategory.TOP.description, price = 10000)
        val brand = Fixtures.brand
        val existingProduct = Product(id = 1L, brand = brand, category = ProductCategory.TOP, price = 10000)
        every { brandService.getBrand(any()) } returns brand
        every { repository.findByBrandIdAndCategory(any(), any()) } returns existingProduct

        // When / Then
        assertThrows<IllegalArgumentException> { service.createProduct(request) }
    }

    @Test
    fun `updateProduct should update and return a product`() {
        // Given
        val id = 1L
        val request = ProductUpdateRequest(category = ProductCategory.PANTS.description, price = 20000)
        val existingProduct = Fixtures.createProduct(id, price = 10000)
        val updatedProduct = Fixtures.createProduct(id, price = 20000)
        every { repository.findById(any()) } returns Optional.of(existingProduct)
        every { repository.findByBrandIdAndCategory(any(), any()) } returns null
        every { repository.save(any()) } returns updatedProduct

        // When
        val result = service.updateProduct(id, request)

        // Then
        assert(result == updatedProduct)
    }

    @Test
    fun `updateProduct should throw NotFound exception when product not found`() {
        // Given
        val id = 1L
        val request = ProductUpdateRequest(category = ProductCategory.PANTS.description, price = 20000)
        every { repository.findById(any()) } returns Optional.empty()

        // When / Then
        assertThrows<NotFound> { service.updateProduct(id, request) }
    }

    @Test
    fun `updateProduct should throw IllegalArgumentException when product with same category already exists`() {
        // Given
        val id = 1L
        val request = ProductUpdateRequest(category = ProductCategory.PANTS.description, price = 20000)
        val brand = Fixtures.brand
        val existingProduct = Fixtures.createProduct(id = id, category = ProductCategory.TOP, price = 10000)
        val anotherProduct = Fixtures.createProduct(id = 2L, category = ProductCategory.PANTS, price = 15000)
        every { repository.findById(any()) } returns Optional.of(existingProduct)
        every { repository.findByBrandIdAndCategory(any(), any()) } returns anotherProduct

        // When / Then
        assertThrows<IllegalArgumentException> { service.updateProduct(id, request) }
    }

    @Test
    fun `deleteProduct should delete a product by id`() {
        // Given
        val id = 1L
        every { repository.findById(any()) } returns Optional.of(Fixtures.product)
        every { repository.delete(any()) } just Runs

        // When
        service.deleteProduct(id)
    }
}