package com.example.maxdoroassigment.domain.repository

import androidx.paging.PagingSource
import com.example.maxdoroassigment.domain.entity.Art

interface ArtRepository {
    fun artPagingSource(): PagingSource<Int, Art>
}