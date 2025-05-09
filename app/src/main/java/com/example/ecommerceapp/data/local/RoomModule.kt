package com.example.ecommerceapp.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesRoomInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context.applicationContext,
        ProductDatabase::class.java,
        "product_db"
    ).build()

    @Singleton
    @Provides
    fun providesDao(db: ProductDatabase) = db.productDao()
}