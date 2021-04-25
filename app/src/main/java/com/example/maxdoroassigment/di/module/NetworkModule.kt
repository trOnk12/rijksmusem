package com.example.maxdoroassigment.di.module

import com.example.maxdoroassigment.data.api.RijksMuseumApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRijksMuseumService(retrofit: Retrofit): RijksMuseumApi =
         retrofit.create(RijksMuseumApi::class.java)

}