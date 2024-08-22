package com.musinsa.shop.initializer

import com.musinsa.shop.brand.BrandService
import com.musinsa.shop.brand.dto.BrandCreateRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths


@Component
@Transactional
class CsvInitializer(
    @Value("classpath:data.csv")
    private val resource: Resource,
    private val brandService: BrandService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Read CSV from resource
        val lines = Files.readAllLines(Paths.get(resource.uri), StandardCharsets.UTF_8)
        lines.removeFirst() // Remove header line

        // Parse CSV to DTO
        val dataList = lines.map { line -> line.split(",") }
            .map {
                InitData(
                    brandName = it[0],
                    topPrice = it[1].toInt(),
                    outerPrice = it[2].toInt(),
                    pantsPrice = it[3].toInt(),
                    sneakersPrice = it[4].toInt(),
                    bagPrice = it[5].toInt(),
                    hatPrice = it[6].toInt(),
                    socksPrice = it[7].toInt(),
                    accessoryPrice = it[8].toInt()
                )
            }

        // Save data to database
        dataList.forEach { data ->
            brandService.createBrand(BrandCreateRequest(data.brandName).toEntity())
        }
    }
}

data class InitData(
    val brandName: String,
    val topPrice: Int,
    val outerPrice: Int,
    val pantsPrice: Int,
    val sneakersPrice: Int,
    val bagPrice: Int,
    val hatPrice: Int,
    val socksPrice: Int,
    val accessoryPrice: Int
)