package com.musinsa.shop.minproduct

import com.musinsa.shop.product.ProductEvent
import com.musinsa.shop.product.ProductEventType
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger { }

@Component
class MinProductUpdateListener(
    private val minProductService: MinProductService
) : ApplicationListener<ProductEvent> {
    override fun onApplicationEvent(event: ProductEvent) {
        log.info { "Event received $event" }

        when (event.eventType) {
            ProductEventType.CREATED, ProductEventType.UPDATED -> {
                minProductService.updatePriceChanged(event.category)
            }

            ProductEventType.DELETED -> {
                minProductService.updateDeleted(category = event.category)
            }
        }
    }
}