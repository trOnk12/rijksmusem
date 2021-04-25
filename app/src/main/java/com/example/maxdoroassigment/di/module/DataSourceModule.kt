package com.example.maxdoroassigment.di.module

import com.example.maxdoroassigment.data.api.RijksMuseumApi
import com.example.maxdoroassigment.data.mapper.ArtResponseMapper
import com.example.maxdoroassigment.data.source.paging.ArtPagingSource
import com.example.maxdoroassigment.data.source.paging.RemoteArtPagingSource
import com.example.maxdoroassigment.data.source.remote.ArtRemoteSource
import com.example.maxdoroassigment.data.source.remote.ArtRemoteSourceRijksMuseumService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataSourceModule {

    @Singleton
    @Provides
    fun provideArtRemoteSource(
        rijksMuseumApi: RijksMuseumApi,
        artResponseMapper: ArtResponseMapper
    ): ArtRemoteSource {
        return ArtRemoteSourceRijksMuseumService(rijksMuseumApi, artResponseMapper)
    }

    @Singleton
    @Provides
    fun providePagingSource(
        artRemoteSource: ArtRemoteSource
    ): ArtPagingSource {
        return RemoteArtPagingSource(artRemoteSource)
    }

}