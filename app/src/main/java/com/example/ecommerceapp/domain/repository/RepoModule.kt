package com.example.ecommerceapp.domain.repository

import com.example.ecommerceapp.data.repository.GetProductRepositoryImpl
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
        getProductRepositoryImpl: GetProductRepositoryImpl
    ): GetProductsRepository
}