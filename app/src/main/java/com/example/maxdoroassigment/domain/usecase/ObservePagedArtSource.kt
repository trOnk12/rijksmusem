package com.example.maxdoroassigment.domain.usecase

import androidx.paging.*
import com.example.maxdoroassigment.data.repository.ArtRepositoryImpl
import com.example.maxdoroassigment.domain.entity.Art
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class ObservePagedArtSource @Inject constructor(
    private val artRepositoryImpl: ArtRepositoryImpl
) {

    fun createFlowPagingData(): Flow<PagingData<Art>> {
        return Pager(
            config = PagingConfig(50),
            pagingSourceFactory = artRepositoryImpl::artPagingSource
        ).flow
    }

}