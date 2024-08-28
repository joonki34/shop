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
        // given
        val brand = Fixtures.brand
        every { repository.save(any()) } returns brand

        // when
        val createdBrand = service.createBrand(brand)

        // then
        assert(createdBrand == brand)
    }

    @Test
    fun `getBrand should return a brand by id`() {
        // given
        val brand = Fixtures.brand
        every { repository.findById(any()) } returns Optional.of(brand)

        // when
        val foundBrand = service.getBrand(1L)

        // then
        assert(foundBrand == brand)
    }

    @Test
    fun `getBrand should throw NotFound exception when brand not found`() {
        // given
        val id = 1L
        every { repository.findById(any()) } returns Optional.empty()

        // when / Then
        assertThrows<NotFound> { service.getBrand(id) }
    }

}