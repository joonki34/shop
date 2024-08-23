package com.musinsa.shop.brand

import com.musinsa.shop.brand.dto.BrandCreateRequest
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BrandController::class)
class BrandControllerTest : RestDocCommon() {
    @MockkBean
    lateinit var service: BrandService

    @Test
    fun getBrand() {
        // given
        every { service.getBrand(any()) } returns Brand(id = 1L, name = "test")

        // when
        val result = mockMvc.perform(get("/v1/brands/{id}", 1L))

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "brand/get", pathParameters(
                        parameterWithName("id").description("ID")
                    ), responseFields(
                        fieldWithPath("id").description("ID"),
                        fieldWithPath("name").description("브랜드 이름")
                    )
                )
            )
    }

    @Test
    fun createBrand() {
        // given
        every { service.createBrand(any()) } returns Brand(id = 1L, name = "test")

        // when
        val result = mockMvc.perform(
            post("/v1/brands")
                .content(objectMapper.writeValueAsBytes(BrandCreateRequest(name = "test")))
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(
                document(
                    "brand/create", requestFields(
                        fieldWithPath("name").description("브랜드 이름")
                    ), responseFields(
                        fieldWithPath("id").description("ID"),
                        fieldWithPath("name").description("브랜드 이름")
                    )
                )
            )
    }

    @Test
    fun deleteBrand() {
        // given
        every { service.deleteBrand(any()) } just Runs

        // when
        val result = mockMvc.perform(
            delete("/v1/brands/{id}", 1L)
        )

        // then
        result.andExpect(status().isNoContent)
            .andDo(
                document(
                    "brand/delete", pathParameters(
                        parameterWithName("id").description("ID")
                    )
                )
            )
    }
}