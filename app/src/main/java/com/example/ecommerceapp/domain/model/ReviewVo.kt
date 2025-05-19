package com.example.ecommerceapp.domain.model

import java.io.Serializable

data class ReviewVo(
    val comment: String?,
    val date: String?,
    val rating: Int?,
    val reviewerEmail: String?,
    val reviewerName: String?
): Serializable
