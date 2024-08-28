package com.musinsa.shop.product

import com.musinsa.shop.product.dto.ProductCreateRequest
import com.musinsa.shop.product.dto.ProductResponse
import com.musinsa.shop.product.dto.ProductUpdateRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/products")
class ProductController(private val service: ProductService) {
    @GetMapping("")
    fun getProductList(@RequestParam("brandName", required = false) brandName: String?): List<ProductResponse> {
        return service.getProductList(brandName).map { ProductResponse.of(it) }
    }

    @GetMapping("/{id}")
    fun getProduct(
        @PathVariable("id") id: Long
    ): ProductResponse {
        return ProductResponse.of(service.getProduct(id))
    }

    @PostMapping("")
    fun createProduct(@RequestBody @Valid request: ProductCreateRequest): ProductResponse {
        return ProductResponse.of(service.createProduct(request))
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable("id") id: Long,
        @RequestBody @Valid request: ProductUpdateRequest
    ): ProductResponse {
        return ProductResponse.of(service.updateProduct(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(
        @PathVariable("id") id: Long
    ) {
        service.deleteProduct(id)
    }
}
