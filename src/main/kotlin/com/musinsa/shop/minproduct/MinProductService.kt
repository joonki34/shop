package com.musinsa.shop.minproduct

import com.musinsa.shop.product.ProductCategory
import com.musinsa.shop.product.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MinProductService(
    private val minProductRepository: MinProductRepository,
    private val productRepository: ProductRepository
) {
    @Transactional
    fun updatePriceChanged(category: ProductCategory) {
        val minProduct = minProductRepository.findByCategory(category)
        val newProduct = productRepository.findFirstByCategoryOrderByPrice(category) ?: return

        if (minProduct == null) {
            // Save MinProduct if not exists
            minProductRepository.save(MinProduct(category = category, productId = newProduct.id!!))
        } else {
            // Update MinProduct if minimum price changed
            val currentProduct = productRepository.findById(minProduct.productId).get()
            if (currentProduct.price > newProduct.price) {
                minProductRepository.save(minProduct.updateProductId(newProduct.id!!))
            }
        }
    }

    @Transactional
    fun updateDeleted(category: ProductCategory) {
        val minProduct = minProductRepository.findByCategory(category) ?: return

        val product = productRepository.findFirstByCategoryOrderByPrice(category) ?: return

        if (minProduct.productId != product.id) {
            minProductRepository.save(minProduct.updateProductId(product.id!!))
        }
    }
}