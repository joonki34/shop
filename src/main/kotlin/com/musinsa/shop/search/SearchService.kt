package com.musinsa.shop.search

import com.musinsa.shop.brand.Brand
import com.musinsa.shop.brand.BrandRepository
import com.musinsa.shop.exception.NotFound
import com.musinsa.shop.minproduct.MinProductRepository
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
class SearchService(
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository,
    private val minProductRepository: MinProductRepository
) {
    @Cacheable(cacheNames = ["minProductByCategoryCache"])
    fun findMinProductByCategory(): List<Product> {
        return minProductRepository.findAllProduct()
    }

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

    @Cacheable(cacheNames = ["minBrandCache"])
    fun findMinBrand(): Pair<Brand, List<Product>> {
        val brand = brandRepository.findFirstByOrderByTotalPrice() ?: throw NotFound("Product not found")

        val list = productRepository.findByBrandId(brand.id!!)
        return brand to list
    }
}