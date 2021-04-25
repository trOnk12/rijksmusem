package com.example.maxdoroassigment.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.maxdoroassigment.data.source.remote.ArtRemoteSource
import com.example.maxdoroassigment.domain.entity.Art
import java.lang.Exception
import javax.inject.Inject

interface ArtPagingSource {
     fun getPagingSource(): PagingSource<Int, Art>
}

class RemoteArtPagingSource @Inject constructor(
    private val artRemoteSource: ArtRemoteSource
) : ArtPagingSource {
    companion object {
        const val DEFAULT_PAGE_INDEX = 0
    }

    override fun getPagingSource(): PagingSource<Int, Art> {
        return object : PagingSource<Int, Art>() {

            override fun getRefreshKey(state: PagingState<Int, Art>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Art> {
                return try {
                    val nextPageNumber = params.key ?: DEFAULT_PAGE_INDEX
                    val response = artRemoteSource.getArtByPage(nextPageNumber)
                    LoadResult.Page(
                        data = response,
                        prevKey = null,
                        nextKey = nextPageNumber.plus(1)
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }
        }
    }
}