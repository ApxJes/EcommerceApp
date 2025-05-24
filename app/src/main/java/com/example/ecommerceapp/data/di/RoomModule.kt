package com.example.ecommerceapp.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ecommerceapp.data.local.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE products_table ADD COLUMN images TEXT")
        }
    }

    @Singleton
    @Provides
    fun providesRoomInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context.applicationContext,
        ProductDatabase::class.java,
        "product_db"
    ).addMigrations(MIGRATION_1_2).build()

    @Singleton
    @Provides
    fun providesDao(db: ProductDatabase) = db.productDao()
}