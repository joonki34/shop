package com.musinsa.shop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class ShopApplication

fun main(args: Array<String>) {
	runApplication<ShopApplication>(*args)
}
