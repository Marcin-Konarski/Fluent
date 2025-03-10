package com.example.fluent.di

import com.example.fluent.data.DataRepository
import com.example.fluent.data.DataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindDataRepository(
        dataRepositoryImpl: DataRepositoryImpl
    ): DataRepository
}