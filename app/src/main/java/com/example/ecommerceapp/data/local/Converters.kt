package com.example.ecommerceapp.data.local

import androidx.room.TypeConverter
import com.example.ecommerceapp.domain.model.ReviewVo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromReviewList(reviews: List<ReviewVo?>?): String {
        return Gson().toJson(reviews)
    }

    @TypeConverter
    fun toReviewList(json: String): List<ReviewVo?>? {
        val type = object : TypeToken<List<ReviewVo?>>() {}.type
        return Gson().fromJson(json, type)
    }
}
