package com.musinsa.shop.search

import com.musinsa.shop.product.Product
import com.musinsa.shop.product.ProductCategory
import com.musinsa.shop.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SearchService(private val productRepository: ProductRepository) {
    @Cacheable(cacheNames = ["minMaxProductCache"], key = "#category")
    fun findMinMaxProductByCategory(category: ProductCategory): Pair<Product?, Product?> {
        val (lowest, highest, _) = runBlocking(Dispatchers.IO) {
            val deferred = listOf(
                async { productRepository.findFirstByCategoryOrderByPrice(category) },
                async { productRepository.findFirstByCategoryOrderByPriceDesc(category) }
            )

            deferred.awaitAll()
        }

        return lowest to highest
    }
}