package com.musinsa.shop.product

import org.springframework.context.ApplicationEvent

class ProductEvent(source: Any, val eventType: ProductEventType, val category: ProductCategory, val id: Long) :
    ApplicationEvent(source) {
    companion object {
        fun of(source: Any, eventType: ProductEventType, product: Product): ProductEvent {
            return ProductEvent(this, eventType, product.category, product.id!!)
        }
    }
}

enum class ProductEventType {
    CREATED, UPDATED, DELETED
}