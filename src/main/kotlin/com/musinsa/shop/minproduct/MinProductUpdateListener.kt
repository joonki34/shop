package com.musinsa.shop.minproduct

import com.musinsa.shop.product.ProductEvent
import com.musinsa.shop.product.ProductEventType
import com.musinsa.shop.product.ProductRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger { }

@Component
class MinProductUpdateListener(
    private val minProductRepository: MinProductRepository,
    private val productRepository: ProductRepository
) : ApplicationListener<ProductEvent> {
    override fun onApplicationEvent(event: ProductEvent) {
        log.info { "Event received $event" }

        val minProduct = minProductRepository.findByCategory(event.category)

        when (event.eventType) {
            ProductEventType.CREATED, ProductEventType.UPDATED -> {
                val product = productRepository.findById(event.id).get()

                if (minProduct == null) {
                    minProductRepository.save(MinProduct(category = event.category, product = product))
                } else {
                    minProductRepository.save(minProduct.updateProduct(product))
                }
            }

            ProductEventType.DELETED -> {
                if (minProduct == null) return

                val product = productRepository.findFirstByCategoryOrderByPrice(event.category) ?: return
                minProductRepository.save(minProduct.updateProduct(product))
            }
        }
    }
}