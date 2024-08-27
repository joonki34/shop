package com.musinsa.shop.initializer

import com.musinsa.shop.brand.BrandService
import com.musinsa.shop.brand.dto.BrandCreateRequest
import com.musinsa.shop.exception.InternalServerError
import com.musinsa.shop.product.ProductCategory
import com.musinsa.shop.product.ProductService
import com.musinsa.shop.product.dto.ProductCreateRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

private val log = KotlinLogging.logger { }

@Component
@Transactional
class CsvInitializer(
    @Value("classpath:data.csv")
    private val resource: Resource,
    private val brandService: BrandService,
    private val productService: ProductService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Read CSV from resource
        val lines = Files.readAllLines(Paths.get(resource.uri), StandardCharsets.UTF_8)

        if (lines.isEmpty()) {
            throw InternalServerError("CSV file is empty")
        }

        lines.removeFirst() // Remove header line

        // Parse CSV to DTO
        val dataList = parseCsv(lines)

        // Save data to database
        saveDatabase(dataList)
    }

    private fun parseCsv(lines: MutableList<String>) = lines.map { line -> line.split(",") }
        .filter {
            if (it.size != COLUMN_SIZE) {
                log.error { "Invalid row: $it" }
            }
            it.size == COLUMN_SIZE
        }
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

    private fun saveDatabase(dataList: List<InitData>) {
        dataList.forEach { data ->
            val brand = brandService.createBrand(BrandCreateRequest(data.brandName).toEntity())
            val brandId = brand.id!!
            listOf(
                ProductCategory.TOP to data.topPrice,
                ProductCategory.OUTER to data.outerPrice,
                ProductCategory.PANTS to data.pantsPrice,
                ProductCategory.SNEAKERS to data.sneakersPrice,
                ProductCategory.BAG to data.bagPrice,
                ProductCategory.HAT to data.hatPrice,
                ProductCategory.SOCKS to data.socksPrice,
                ProductCategory.ACCESSORY to data.accessoryPrice,
            ).forEach {
                productService.createProduct(ProductCreateRequest(brandId, it.first.description, it.second))
            }
        }
    }

    companion object {
        private const val COLUMN_SIZE = 9
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