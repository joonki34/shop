package com.musinsa.shop.search

import com.musinsa.shop.product.ProductCategory
import com.musinsa.shop.search.dto.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/search")
class SearchController(private val service: SearchService) {
    @GetMapping("/min-product-by-category")
    fun findMinProductByCategory(): MinProductByCategoryResponse {
        return MinProductByCategoryResponse.of(service.findMinProductByCategory())
    }

    @GetMapping("/min-brand")
    fun findMinBrand(): MinBrandResponse {
        val (brand, list) = service.findMinBrand()
        return MinBrandResponse.of(brand, list)
    }

    @PostMapping("/min-max-price")
    fun findMinMaxProductByCategory(@RequestBody request: ProductByCategoryRequest): ProductByCategoryResponse {
        val (lowest, highest) = service.findMinMaxProductByCategory(ProductCategory.descriptionMap[request.category]!!)
        return ProductByCategoryResponse(
            category = request.category,
            min = lowest?.let { ProductByCategoryDetail.of(it) },
            max = highest?.let { ProductByCategoryDetail.of(it) }
        )
    }
}