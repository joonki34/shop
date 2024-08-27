package com.musinsa.shop.search

import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.product.ProductCategory
import com.musinsa.shop.product.ProductRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SearchServiceTest {

    private val productRepository = mockk<ProductRepository>()
    private val searchService = SearchService(productRepository)

    @Test
    fun `findMinMaxProductByCategory should return the lowest and highest priced products for the given category`() {
        val category = ProductCategory.TOP
        val lowest = Fixtures.product
        val highest = Fixtures.product
        every { productRepository.findFirstByCategoryOrderByPrice(category) } returns lowest
        every { productRepository.findFirstByCategoryOrderByPriceDesc(category) } returns highest

        val (actualLowest, actualHighest) = searchService.findMinMaxProductByCategory(category)

        assertEquals(lowest, actualLowest)
        assertEquals(highest, actualHighest)
    }

    @Test
    fun `findMinMaxProductByCategory should return null for lowest or highest if no products exist for the given category`() {
        val category = ProductCategory.TOP
        every { productRepository.findFirstByCategoryOrderByPrice(category) } returns null
        every { productRepository.findFirstByCategoryOrderByPriceDesc(category) } returns null

        val (actualLowest, actualHighest) = searchService.findMinMaxProductByCategory(category)

        assertEquals(null, actualLowest)
        assertEquals(null, actualHighest)
    }
}