package com.example.maxdoroassigment.presentation.ui.artlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.maxdoroassigment.core.extension.flatMapPage
import com.example.maxdoroassigment.domain.usecase.ObservePagedArtSource
import com.example.maxdoroassigment.presentation.model.ArtListItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
class ArtListViewModel
@Inject constructor(
    private val observedPagedArtSource: ObservePagedArtSource
) : ViewModel() {

    val artListItemPagingData = MutableLiveData<PagingData<ArtListItem>>()

    init {
        viewModelScope.launch {
            observedPagedArtSource
                .createFlowPagingData()
                .flatMapPage { art ->
                    with(art) {
                        ArtListItem(
                            id,
                            artDescriptionInfo.title,
                            artDescriptionInfo.webImageUrl
                        )
                    }
                }.cachedIn(viewModelScope).collect { pagingData ->
                    artListItemPagingData.value = pagingData
                }
        }
    }

}