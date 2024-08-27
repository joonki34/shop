package com.musinsa.shop.minproduct

import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.product.ProductEventType
import com.musinsa.shop.product.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*

class MinProductUpdateListenerTest {
    private val minProductRepository = mockk<MinProductRepository>()
    private val productRepository = mockk<ProductRepository>()
    private val listener = MinProductUpdateListener(minProductRepository, productRepository)

    @Test
    fun `onApplicationEvent should create new MinProduct when product is created and no MinProduct exists`() {
        // given
        val product = Fixtures.product
        val event = Fixtures.createProductEvent()

        every { minProductRepository.findByCategory(event.category) } returns null
        every { productRepository.findById(event.id) } returns Optional.of(product)
        every { minProductRepository.save(any()) } returnsArgument 0

        // when
        listener.onApplicationEvent(event)

        // then
        verify(exactly = 1) { minProductRepository.findByCategory(event.category) }
        verify(exactly = 1) { productRepository.findById(event.id) }
        verify(exactly = 1) { minProductRepository.save(any()) }
    }

    @Test
    fun `onApplicationEvent should update existing MinProduct when product is created and MinProduct exists`() {
        // given
        val product = Fixtures.product
        val event = Fixtures.createProductEvent()
        val existingMinProduct = Fixtures.createMinProduct()

        every { minProductRepository.findByCategory(event.category) } returns existingMinProduct
        every { productRepository.findById(event.id) } returns Optional.of(product)
        every { minProductRepository.save(any()) } returnsArgument 0

        // when
        listener.onApplicationEvent(event)

        // then
        verify(exactly = 1) { minProductRepository.findByCategory(event.category) }
        verify(exactly = 1) { productRepository.findById(event.id) }
        verify(exactly = 1) { minProductRepository.save(existingMinProduct.updateProduct(product)) }
    }

    @Test
    fun `onApplicationEvent should update existing MinProduct when product is updated`() {
        val event = Fixtures.createProductEvent()
        val product = Fixtures.product
        val existingMinProduct = Fixtures.createMinProduct()

        every { minProductRepository.findByCategory(event.category) } returns existingMinProduct
        every { productRepository.findById(event.id) } returns Optional.of(product)
        every { minProductRepository.save(any()) } returnsArgument 0

        listener.onApplicationEvent(event)

        verify(exactly = 1) { minProductRepository.findByCategory(event.category) }
        verify(exactly = 1) { productRepository.findById(event.id) }
        verify(exactly = 1) { minProductRepository.save(existingMinProduct.updateProduct(product)) }
    }

    @Test
    fun `onApplicationEvent should update existing MinProduct when product is deleted and new min price product exists`() {
        val event = Fixtures.createProductEvent(eventType = ProductEventType.DELETED)
        val product = Fixtures.product
        val existingMinProduct = Fixtures.createMinProduct()

        every { minProductRepository.findByCategory(event.category) } returns existingMinProduct
        every { productRepository.findFirstByCategoryOrderByPrice(event.category) } returns product
        every { minProductRepository.save(any()) } returnsArgument 0

        listener.onApplicationEvent(event)

        verify(exactly = 1) { minProductRepository.findByCategory(event.category) }
        verify(exactly = 1) { productRepository.findFirstByCategoryOrderByPrice(event.category) }
        verify(exactly = 1) { minProductRepository.save(existingMinProduct.updateProduct(product)) }
    }

    @Test
    fun `onApplicationEvent should do nothing when product is deleted and no MinProduct exists`() {
        val event = Fixtures.createProductEvent(eventType = ProductEventType.DELETED)

        every { minProductRepository.findByCategory(event.category) } returns null

        listener.onApplicationEvent(event)

        verify(exactly = 1) { minProductRepository.findByCategory(event.category) }
        verify(exactly = 0) { productRepository.findFirstByCategoryOrderByPrice(any()) }
        verify(exactly = 0) { minProductRepository.save(any()) }
    }

    @Test
    fun `onApplicationEvent should do nothing when product is deleted and no other product exists in the category`() {
        val event = Fixtures.createProductEvent(eventType = ProductEventType.DELETED)
        val existingMinProduct = Fixtures.createMinProduct()

        every { minProductRepository.findByCategory(event.category) } returns existingMinProduct
        every { productRepository.findFirstByCategoryOrderByPrice(event.category) } returns null

        listener.onApplicationEvent(event)

        verify(exactly = 1) { minProductRepository.findByCategory(event.category) }
        verify(exactly = 1) { productRepository.findFirstByCategoryOrderByPrice(event.category) }
        verify(exactly = 0) { minProductRepository.save(any()) }
    }
}