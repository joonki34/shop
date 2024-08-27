package com.musinsa.shop.brand

import com.musinsa.shop.brand.dto.BrandCreateRequest
import com.musinsa.shop.brand.dto.BrandResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/brands")
class BrandController(private val service: BrandService) {
    @GetMapping("/{id}")
    fun getBrand(@PathVariable("id") id: Long): BrandResponse {
        return BrandResponse.of(service.getBrand(id))
    }

    @PostMapping
    fun createBrand(@RequestBody @Valid request: BrandCreateRequest): BrandResponse {
        return BrandResponse.of(service.createBrand(request.toEntity()))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBrand(@PathVariable("id") id: Long) {
        service.deleteBrand(id)
    }
}