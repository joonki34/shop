package com.musinsa.shop.search

import com.musinsa.shop.fixtures.Fixtures
import com.musinsa.shop.restdocs.RestDocCommon
import com.musinsa.shop.search.dto.ProductByCategoryRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(SearchController::class)
class SearchControllerTest : RestDocCommon() {
    @MockkBean
    lateinit var service: SearchService

    @Test
    fun findMinBrand() {
        // given
        every { service.findMinBrand() } returns (Fixtures.brand to listOf(Fixtures.product))

        // when
        val result = mockMvc.perform(get("/v1/search/min-brand"))

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "search/min-brand", responseFields(
                        fieldWithPath("min.brandName").description("브랜드 이름"),
                        fieldWithPath("min.categoryList").type(JsonFieldType.ARRAY).description("상품 리스트"),
                        fieldWithPath("min.categoryList[].id").description("상품 ID"),
                        fieldWithPath("min.categoryList[].category").description("카테고리"),
                        fieldWithPath("min.categoryList[].price").description("상품 가격"),
                        fieldWithPath("min.total").description("총 가격"),
                    )
                )
            )
    }

    @Test
    fun findMinMaxProductByCategory() {
        // given
        every { service.findMinMaxProductByCategory(any()) } returns (Fixtures.product to Fixtures.product)

        // when
        val result = mockMvc.perform(
            post("/v1/search/min-max-price")
                .content(objectMapper.writeValueAsBytes(ProductByCategoryRequest(category = "상의")))
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "search/min-max-price", requestFields(
                        fieldWithPath("category").description("카테고리")
                    ), responseFields(
                        fieldWithPath("category").description("카테고리"),
                        fieldWithPath("min.brandName").description("최저가 브랜드명"),
                        fieldWithPath("min.price").description("최저가 상품 가격"),
                        fieldWithPath("max.brandName").description("최고가 브랜드명"),
                        fieldWithPath("max.price").description("최고가 상품 가격"),
                    )
                )
            )
    }
}