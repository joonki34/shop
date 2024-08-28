package com.musinsa.shop.minproduct

import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.product.ProductEventType
import io.mockk.*
import org.junit.jupiter.api.Test

class MinProductUpdateListenerTest {
    private val minProductService = mockk<MinProductService>()
    private val listener = MinProductUpdateListener(minProductService)

    @Test
    fun `onApplicationEvent should update price changed when event type is CREATED`() {
        val event = Fixtures.createProductEvent(eventType = ProductEventType.CREATED)

        every { minProductService.updatePriceChanged(any()) } just Runs

        listener.onApplicationEvent(event)

        verify(exactly = 1) { minProductService.updatePriceChanged(event.category) }
    }

    @Test
    fun `onApplicationEvent should update price changed when event type is UPDATED`() {
        val event = Fixtures.createProductEvent(eventType = ProductEventType.UPDATED)

        every { minProductService.updatePriceChanged(any()) } just runs

        listener.onApplicationEvent(event)

        verify(exactly = 1) { minProductService.updatePriceChanged(event.category) }
    }

    @Test
    fun `onApplicationEvent should update deleted when event type is DELETED`() {
        val event = Fixtures.createProductEvent(eventType = ProductEventType.DELETED)

        every { minProductService.updateDeleted(any()) } just runs

        listener.onApplicationEvent(event)

        verify(exactly = 1) { minProductService.updateDeleted(event.category) }
    }
}