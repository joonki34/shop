package com.musinsa.shop.minproduct

import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.product.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*

class MinProductServiceTest {
    private val minProductRepository = mockk<MinProductRepository>()
    private val productRepository = mockk<ProductRepository>()
    private val service = MinProductService(minProductRepository, productRepository)

    @Test
    fun `updatePriceChanged should save MinProduct if not exists`() {
        // given
        val newProduct = Fixtures.product
        every { minProductRepository.findByCategory(any()) } returns null
        every { productRepository.findFirstByCategoryOrderByPrice(any()) } returns newProduct
        every { minProductRepository.save(any()) } returns MinProduct(
            category = Fixtures.product.category,
            productId = newProduct.id!!
        )

        // when
        service.updatePriceChanged(Fixtures.product.category)

        // then
        verify(exactly = 0) { productRepository.findById(any()) }
    }

    @Test
    fun `updatePriceChanged should update MinProduct if minimum price changed`() {
        // given
        val currentProduct = Fixtures.createProduct(id = 1L, price = 20)
        val minProduct = Fixtures.createMinProduct(productId = 1L)
        val newProduct = Fixtures.createProduct(id = 2, price = 10)
        every { minProductRepository.findByCategory(currentProduct.category) } returns minProduct
        every { productRepository.findFirstByCategoryOrderByPrice(currentProduct.category) } returns newProduct
        every { productRepository.findById(minProduct.productId) } returns Optional.of(currentProduct)
        every { minProductRepository.save(any()) } returnsArgument 0

        // when
        service.updatePriceChanged(currentProduct.category)

        // then
        verify(exactly = 1) { productRepository.findById(any()) }
    }

    @Test
    fun `updatePriceChanged should not update MinProduct if minimum price not changed`() {
        // given
        val currentProduct = Fixtures.createProduct(price = 20)
        val minProduct = Fixtures.createMinProduct()
        val newProduct = Fixtures.createProduct(id = 2, price = 30)
        every { minProductRepository.findByCategory(currentProduct.category) } returns minProduct
        every { productRepository.findFirstByCategoryOrderByPrice(currentProduct.category) } returns newProduct
        every { productRepository.findById(minProduct.productId) } returns Optional.of(currentProduct)
        every { minProductRepository.save(any()) } returnsArgument 0

        // when
        service.updatePriceChanged(currentProduct.category)

        // then
        verify(exactly = 0) { minProductRepository.save(any()) }
    }

    @Test
    fun `updatePriceChanged should not update MinProduct if new product is null`() {
        // given
        every { minProductRepository.findByCategory(any()) } returns Fixtures.createMinProduct()
        every { productRepository.findFirstByCategoryOrderByPrice(any()) } returns null

        // when
        service.updatePriceChanged(Fixtures.product.category)

        // then
        verify(exactly = 0) { minProductRepository.save(any()) }
    }

    @Test
    fun `updateDeleted should update MinProduct if product id is different`() {
        // given
        val minProduct = Fixtures.createMinProduct()
        val product = Fixtures.createProduct(id = 2L)
        every { minProductRepository.findByCategory(any()) } returns minProduct
        every { productRepository.findFirstByCategoryOrderByPrice(any()) } returns product
        every { minProductRepository.save(any()) } returnsArgument 0

        // when
        service.updateDeleted(product.category)

        // then
        verify(exactly = 1) { minProductRepository.save(any()) }
    }

    @Test
    fun `updateDeleted should not update MinProduct if product id is same`() {
        // given
        val minProduct = Fixtures.createMinProduct()
        val product = Fixtures.createProduct()
        every { minProductRepository.findByCategory(any()) } returns minProduct
        every { productRepository.findFirstByCategoryOrderByPrice(any()) } returns product
        every { minProductRepository.save(any()) } returnsArgument 0

        // when
        service.updateDeleted(product.category)

        // then
        verify(exactly = 0) { minProductRepository.save(any()) }
    }

    @Test
    fun `updateDeleted should not update MinProduct if minProduct is null`() {
        // given
        every { minProductRepository.findByCategory(any()) } returns null

        // when
        service.updateDeleted(Fixtures.product.category)

        // then
        verify(exactly = 0) { minProductRepository.save(any()) }
    }

    @Test
    fun `updateDeleted should not update MinProduct if product is null`() {
        // given
        val minProduct = Fixtures.createMinProduct()
        every { minProductRepository.findByCategory(any()) } returns minProduct
        every { productRepository.findFirstByCategoryOrderByPrice(any()) } returns null

        // when
        service.updateDeleted(Fixtures.product.category)

        // then
        verify(exactly = 0) { minProductRepository.save(any()) }
    }
}