package com.example.ecommerceapp.data.repository

import com.example.ecommerceapp.data.local.ProductDao
import com.example.ecommerceapp.data.remote.ItemsApi
import com.example.ecommerceapp.data.remote.dto.ItemsDto
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.domain.repository.GetProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductRepositoryImpl @Inject constructor(
    private val api: ItemsApi,
    private val dao: ProductDao
): GetProductsRepository {
    override suspend fun getAllProducts(): ItemsDto {
        return api.getItems()
    }

    override suspend fun getProductsByCategory(category: String): ItemsDto {
        return api.getProductsByCategory(category)
    }

    override fun getProducts(): Flow<List<Product>> {
        return dao.getProducts()
    }

    override suspend fun insertProduct(product: Product) = dao.insertProduct(product)


    override suspend fun deleteProduct(product: Product) = dao.deleteProduct(product)

}