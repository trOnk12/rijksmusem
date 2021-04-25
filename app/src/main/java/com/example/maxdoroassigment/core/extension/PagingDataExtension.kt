package com.example.maxdoroassigment.core.extension

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T : Any, R : Any> Flow<PagingData<T>>.flatMapPage(map: (T) -> R): Flow<PagingData<R>> {
   return map { it.map { item -> map(item) } }
}