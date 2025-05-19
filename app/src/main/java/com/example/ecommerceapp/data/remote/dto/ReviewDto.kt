package com.example.ecommerceapp.data.remote.dto


import com.example.ecommerceapp.domain.model.ReviewVo
import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("rating")
    val rating: Int?,
    @SerializedName("reviewerEmail")
    val reviewerEmail: String?,
    @SerializedName("reviewerName")
    val reviewerName: String?
) {
    fun toReview(): ReviewVo {
        return ReviewVo(
            comment = comment,
            date = date,
            rating = rating,
            reviewerName = reviewerName,
            reviewerEmail = reviewerEmail
        )
    }
}