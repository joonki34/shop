package com.musinsa.shop.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringUtilTest {

    @Test
    fun `toPriceString formats positive integer correctly`() {
        val amount = 12345
        val expected = "12,345"
        assertEquals(expected, amount.toPriceString())
    }

    @Test
    fun `toPriceString formats zero correctly`() {
        val amount = 0
        val expected = "0"
        assertEquals(expected, amount.toPriceString())
    }

    @Test
    fun `toPriceString formats negative integer correctly`() {
        val amount = -12345
        val expected = "-12,345"
        assertEquals(expected, amount.toPriceString())
    }
}