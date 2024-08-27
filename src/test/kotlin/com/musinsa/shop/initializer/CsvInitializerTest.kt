package com.musinsa.shop.initializer

import com.musinsa.shop.brand.BrandService
import com.musinsa.shop.exception.InternalServerError
import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.product.ProductService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.core.io.ClassPathResource

class CsvInitializerTest {
    private val brandService = mockk<BrandService>()
    private val productService = mockk<ProductService>()

    @BeforeEach
    fun setUp() {
        every { brandService.createBrand(any()) } returns Fixtures.brand
        every { productService.createProduct(any()) } returns Fixtures.product
    }

    @Test
    fun `run should return throw InternalServerError exception if csv file is empty`() {
        val initializer = CsvInitializer(
            ClassPathResource("test-data-empty.csv"),
            brandService,
            productService
        )

        assertThrows<InternalServerError> {
            initializer.run(null)
        }
    }

    @Test
    fun `run should filter invalid row`() {
        CsvInitializer(
            ClassPathResource("test-data-invalid.csv"),
            brandService,
            productService
        ).run(null)


        verify(exactly = 8) { brandService.createBrand(any()) }
        verify(exactly = 64) { productService.createProduct(any()) }
    }

    @Test
    fun `run should parse csv and save to database`() {
        CsvInitializer(
            ClassPathResource("test-data.csv"),
            brandService,
            productService
        ).run(null)

        verify(exactly = 9) { brandService.createBrand(any()) }
        verify(exactly = 72) { productService.createProduct(any()) }
    }

}