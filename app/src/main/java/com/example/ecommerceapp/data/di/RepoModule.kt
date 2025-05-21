package com.example.ecommerceapp.data.di

import com.example.ecommerceapp.data.repository.ProductsRepositoryImpl
import com.example.ecommerceapp.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun providesRepoImpl(
        getProductRepositoryImpl: ProductsRepositoryImpl
    ): ProductsRepository
}