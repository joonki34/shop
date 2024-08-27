package com.musinsa.shop.product

import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.product.dto.ProductCreateRequest
import com.musinsa.shop.product.dto.ProductUpdateRequest
import com.musinsa.shop.restdocs.RestDocCommon
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ProductController::class)
class ProductControllerTest : RestDocCommon() {

    @MockkBean
    lateinit var service: ProductService

    @Test
    fun getProduct() {
        // given
        every { service.getProduct(any()) } returns Fixtures.product

        // when
        val result = mockMvc.perform(get("/v1/products/{id}", 1L))

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "product/get", pathParameters(
                        parameterWithName("id").description("ID")
                    ), productResponseFieldsSnippet()
                )
            )
    }

    @Test
    fun createProduct() {
        // given
        every { service.createProduct(any()) } returns Fixtures.product

        // when
        val result = mockMvc.perform(
            post("/v1/products")
                .content(
                    objectMapper.writeValueAsBytes(
                        ProductCreateRequest(
                            price = 10000,
                            brandId = 1L,
                            category = ProductCategory.BAG.description
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "product/create", requestFields(
                        fieldWithPath("brandId").description("브랜드 ID"),
                        fieldWithPath("category").description("카테고리"),
                        fieldWithPath("price").description("상품 가격"),
                    ), productResponseFieldsSnippet()
                )
            )
    }

    @Test
    fun updateProduct() {
        // given
        every { service.updateProduct(any(), any()) } returns Fixtures.product

        // when
        val result = mockMvc.perform(
            put("/v1/products/{id}", 1L)
                .content(
                    objectMapper.writeValueAsBytes(
                        ProductUpdateRequest(
                            price = 20000,
                            category = ProductCategory.BAG.description
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "product/update",
                    pathParameters(
                        parameterWithName("id").description("ID")
                    ),
                    requestFields(
                        fieldWithPath("category").description("카테고리"),
                        fieldWithPath("price").description("상품 가격"),
                    ),
                    productResponseFieldsSnippet()
                )
            )
    }

    private fun productResponseFieldsSnippet(): ResponseFieldsSnippet? = responseFields(
        fieldWithPath("id").description("ID"),
        fieldWithPath("category").description("카테고리"),
        fieldWithPath("price").description("상품 가격"),
    )

    @Test
    fun deleteProduct() {
        // given
        every { service.deleteProduct(any()) } just Runs

        // when
        val result = mockMvc.perform(
            delete("/v1/products/{id}", 1L)
        )

        // then
        result.andExpect(status().isNoContent)
            .andDo(
                document(
                    "product/delete", pathParameters(
                        parameterWithName("id").description("ID")
                    )
                )
            )
    }
}