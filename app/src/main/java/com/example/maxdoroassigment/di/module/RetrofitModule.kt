package com.example.maxdoroassigment.di.module

import com.example.maxdoroassigment.data.api.AuthenticationInterceptor
import com.example.maxdoroassigment.data.api.KeyAuthenticationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofitClient(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.rijksmuseum.nl/api/nl/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authenticationInterceptor: AuthenticationInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authenticationInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(Level.BODY)
    }

    @Singleton
    @Provides
    fun provideAuthenticationInterceptor(): AuthenticationInterceptor {
        return KeyAuthenticationInterceptor()
    }

}