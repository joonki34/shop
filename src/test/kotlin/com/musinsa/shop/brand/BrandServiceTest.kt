package com.musinsa.shop.brand

import com.musinsa.shop.exception.NotFound
import com.musinsa.shop.fixtures.Fixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class BrandServiceTest {
    private val repository = mockk<BrandRepository>()
    private val service = BrandService(repository)

    @Test
    fun `createBrand should save and return a brand`() {
        // Given
        val brand = Fixtures.brand
        every { repository.save(any()) } returns brand

        // When
        val createdBrand = service.createBrand(brand)

        // Then
        assert(createdBrand == brand)
    }

    @Test
    fun `getBrand should return a brand by id`() {
        // Given
        val brand = Fixtures.brand
        every { repository.findById(any()) } returns Optional.of(brand)

        // When
        val foundBrand = service.getBrand(1L)

        // Then
        assert(foundBrand == brand)
    }

    @Test
    fun `getBrand should throw NotFound exception when brand not found`() {
        // Given
        val id = 1L
        every { repository.findById(any()) } returns Optional.empty()

        // When / Then
        assertThrows<NotFound> { service.getBrand(id) }
    }

}