package com.example.ecommerceapp.data.local

import androidx.room.TypeConverter
import com.example.ecommerceapp.domain.model.Review
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromReviewList(reviews: List<Review?>?): String {
        return Gson().toJson(reviews)
    }

    @TypeConverter
    fun toReviewList(json: String): List<Review?>? {
        val type = object : TypeToken<List<Review?>>() {}.type
        return Gson().fromJson(json, type)
    }
}
