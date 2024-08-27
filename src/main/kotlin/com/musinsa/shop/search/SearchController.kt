package com.musinsa.shop.search

import com.musinsa.shop.product.ProductCategory
import com.musinsa.shop.search.dto.MinBrandResponse
import com.musinsa.shop.search.dto.ProductByCategoryDetail
import com.musinsa.shop.search.dto.ProductByCategoryRequest
import com.musinsa.shop.search.dto.ProductByCategoryResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/search")
class SearchController(private val searchService: SearchService) {
    @GetMapping("/min-brand")
    fun findMinBrand(): MinBrandResponse {
        val (brand, list) = searchService.findMinBrand()
        return MinBrandResponse.of(brand, list)
    }

    @PostMapping("/min-max-price")
    fun findMinMaxProductByCategory(@RequestBody request: ProductByCategoryRequest): ProductByCategoryResponse {
        val (lowest, highest) = searchService.findMinMaxProductByCategory(ProductCategory.descriptionMap[request.category]!!)
        return ProductByCategoryResponse(
            category = request.category,
            min = lowest?.let { ProductByCategoryDetail.of(it) },
            max = highest?.let { ProductByCategoryDetail.of(it) }
        )
    }
}