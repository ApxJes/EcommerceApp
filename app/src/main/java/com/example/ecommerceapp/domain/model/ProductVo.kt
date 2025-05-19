package com.example.ecommerceapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products_table")
data class ProductVo(

    @PrimaryKey(autoGenerate = true) val id: Int?,

    val availabilityStatus: String?,
    val brand: String?,
    val category: String?,
    val description: String?,
    val discountPercentage: Double?,
    val minimumOrderQuantity: Int?,
    val price: Double?,
    val rating: Double?,
    val reviews: List<ReviewVo?>?,
    val stock: Int?,
    val thumbnail: String?,
    val title: String?,
    val warrantyInformation: String?,
    val shippingInformation: String?,
): Serializable
