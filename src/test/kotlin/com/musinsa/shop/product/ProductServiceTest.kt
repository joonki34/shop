package com.musinsa.shop.product

import com.musinsa.shop.brand.BrandService
import com.musinsa.shop.exception.NotFound
import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.product.dto.ProductCreateRequest
import com.musinsa.shop.product.dto.ProductUpdateRequest
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.context.ApplicationEventPublisher
import java.util.*

class ProductServiceTest {

    private val repository = mockk<ProductRepository>()
    private val brandService = mockk<BrandService>()
    private val eventPublisher = mockk<ApplicationEventPublisher>()
    private val service = ProductService(repository, brandService, eventPublisher)

    @BeforeEach
    fun setUp() {
        every { brandService.updatePrice(any(), any()) } just Runs

        every { eventPublisher.publishEvent(any()) } just Runs
    }

    @Test
    fun `getProductList should return all products when brandId is null`() {
        // given
        val brandId = null
        val products = listOf(Fixtures.product)
        every { repository.findAll() } returns products

        // when
        val foundProducts = service.getProductList(brandId)

        // then
        assert(foundProducts == products)
        verify(exactly = 1) { repository.findAll() }
    }

    @Test
    fun `getProductList should return products by brandId when brandId is not null`() {
        // given
        val brandName = "A"
        val products = listOf(Fixtures.product)
        every { repository.findByBrandName(any()) } returns products

        // when
        val foundProducts = service.getProductList(brandName)

        // then
        assert(foundProducts == products)
        verify(exactly = 1) { repository.findByBrandName(brandName) }
    }

    @Test
    fun `getProduct should return a product by id`() {
        // given
        val id = 1L
        val product = Fixtures.createProduct(id)
        every { repository.findById(any()) } returns Optional.of(product)

        // when
        val foundProduct = service.getProduct(id)

        // then
        assert(foundProduct == product)
    }

    @Test
    fun `getProduct should throw NotFound exception when product not found`() {
        // given
        val id = 1L
        every { repository.findById(any()) } returns Optional.empty()

        // when / then
        assertThrows<NotFound> { service.getProduct(id) }
    }

    @Test
    fun `createProduct should save and return a product`() {
        // given
        val request = ProductCreateRequest(brandId = 1L, category = ProductCategory.TOP.description, price = 10000)
        val brand = Fixtures.brand
        val product = Fixtures.product
        every { brandService.getBrand(any()) } returns brand
        every { repository.findByBrandIdAndCategory(any(), any()) } returns null
        every { repository.save(any()) } returns product

        // when
        val createdProduct = service.createProduct(request)

        // then
        assert(createdProduct == product)
        verify(exactly = 1) { brandService.updatePrice(any(), 10000) }
    }

    @Test
    fun `createProduct should throw IllegalArgumentException when product with same category already exists`() {
        // given
        val request = ProductCreateRequest(brandId = 1L, category = ProductCategory.TOP.description, price = 10000)
        val brand = Fixtures.brand
        val existingProduct = Product(id = 1L, brand = brand, category = ProductCategory.TOP, price = 10000)
        every { brandService.getBrand(any()) } returns brand
        every { repository.findByBrandIdAndCategory(any(), any()) } returns existingProduct

        // when / then
        assertThrows<IllegalArgumentException> { service.createProduct(request) }
    }

    @Test
    fun `updateProduct should update and return a product`() {
        // given
        val id = 1L
        val request = ProductUpdateRequest(category = ProductCategory.PANTS.description, price = 30000)
        val existingProduct = Fixtures.createProduct(id, price = 10000)
        val updatedProduct = Fixtures.createProduct(id, price = 30000)
        every { repository.findById(any()) } returns Optional.of(existingProduct)
        every { repository.findByBrandIdAndCategory(any(), any()) } returns null
        every { repository.save(any()) } returns updatedProduct

        // when
        val result = service.updateProduct(id, request)

        // then
        assert(result == updatedProduct)
        verify(exactly = 1) { brandService.updatePrice(any(), 20000) }
    }

    @Test
    fun `updateProduct should throw NotFound exception when product not found`() {
        // given
        val id = 1L
        val request = ProductUpdateRequest(category = ProductCategory.PANTS.description, price = 20000)
        every { repository.findById(any()) } returns Optional.empty()

        // when / Then
        assertThrows<NotFound> { service.updateProduct(id, request) }
    }

    @Test
    fun `updateProduct should throw IllegalArgumentException when product with same category already exists`() {
        // given
        val id = 1L
        val request = ProductUpdateRequest(category = ProductCategory.PANTS.description, price = 20000)
        val existingProduct = Fixtures.createProduct(id = id, category = ProductCategory.TOP, price = 10000)
        val anotherProduct = Fixtures.createProduct(id = 2L, category = ProductCategory.PANTS, price = 15000)
        every { repository.findById(any()) } returns Optional.of(existingProduct)
        every { repository.findByBrandIdAndCategory(any(), any()) } returns anotherProduct

        // when / Then
        assertThrows<IllegalArgumentException> { service.updateProduct(id, request) }
    }

    @Test
    fun `deleteProduct should delete a product by id`() {
        // given
        val id = 1L
        every { repository.findById(any()) } returns Optional.of(Fixtures.product)
        every { repository.delete(any()) } just Runs

        // when
        service.deleteProduct(id)

        // then
        verify(exactly = 1) { brandService.updatePrice(any(), -10000) }
    }
}