package com.musinsa.shop.search

import com.musinsa.shop.brand.BrandRepository
import com.musinsa.shop.exception.NotFound
import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.minproduct.MinProductRepository
import com.musinsa.shop.product.ProductCategory
import com.musinsa.shop.product.ProductRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SearchServiceTest {

    private val productRepository = mockk<ProductRepository>()
    private val brandRepository = mockk<BrandRepository>()
    private val minProductRepository = mockk<MinProductRepository>()
    private val service = SearchService(productRepository, brandRepository, minProductRepository)

    @Test
    fun `findMinProductByCategory should return list of products`() {
        every { minProductRepository.findAllProduct() } returns listOf(
            Fixtures.product
        )

        val result = service.findMinProductByCategory()

        assertEquals(1, result.size)
        assertEquals(ProductCategory.BAG, result[0].category)
    }

    @Test
    fun `findMinMaxProductByCategory should return the lowest and highest priced products for the given category`() {
        val category = ProductCategory.TOP
        val lowest = Fixtures.product
        val highest = Fixtures.product
        every { productRepository.findFirstByCategoryOrderByPrice(category) } returns lowest
        every { productRepository.findFirstByCategoryOrderByPriceDesc(category) } returns highest

        val (actualLowest, actualHighest) = service.findMinMaxProductByCategory(category)

        assertEquals(lowest, actualLowest)
        assertEquals(highest, actualHighest)
    }

    @Test
    fun `findMinMaxProductByCategory should return null for lowest or highest if no products exist for the given category`() {
        val category = ProductCategory.TOP
        every { productRepository.findFirstByCategoryOrderByPrice(category) } returns null
        every { productRepository.findFirstByCategoryOrderByPriceDesc(category) } returns null

        val (actualLowest, actualHighest) = service.findMinMaxProductByCategory(category)

        assertEquals(null, actualLowest)
        assertEquals(null, actualHighest)
    }

    @Test
    fun `findMinBrand should return brand and its products when brand exists`() {
        // Given
        val brand = Fixtures.createBrand(totalPrice = 10000)
        val products = listOf(Fixtures.product)
        every { brandRepository.findFirstByOrderByTotalPrice() } returns brand
        every { productRepository.findByBrandId(brand.id!!) } returns products

        // When
        val result = service.findMinBrand()

        // Then
        assertEquals(brand to products, result)
        assertEquals(10000, result.first.totalPrice)
    }

    @Test
    fun `findMinBrand should throw NotFound exception when brand does not exist`() {
        every { brandRepository.findFirstByOrderByTotalPrice() } returns null

        assertThrows<NotFound> { service.findMinBrand() }
    }
}