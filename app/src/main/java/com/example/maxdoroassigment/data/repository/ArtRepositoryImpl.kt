package com.example.maxdoroassigment.data.repository

import androidx.paging.PagingSource
import com.example.maxdoroassigment.data.source.paging.ArtPagingSource
import com.example.maxdoroassigment.domain.entity.Art
import javax.inject.Inject

class ArtRepositoryImpl @Inject constructor(
    private val artPagingSource: ArtPagingSource
) {

    fun artPagingSource(): PagingSource<Int, Art> = artPagingSource.getPagingSource()

}