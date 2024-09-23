package com.vaibhav.investmentsapp.data.di

import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vaibhav.investmentsapp.data.repositories.InvestmentsRepositoryImpl
import com.vaibhav.investmentsapp.domain.apis.InvestmentsRepositoryApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module providing data layer dependencies for the app.
 */
@Module(
    includes = [
        InvestmentsDataModule.DataBindsModule::class
    ]
)
@InstallIn(SingletonComponent::class)
class InvestmentsDataModule {

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class DataBindsModule {

        @Binds
        @Singleton
        abstract fun bindInvestmentsRepositoryApi(repositoryImpl: InvestmentsRepositoryImpl): InvestmentsRepositoryApi
    }
}