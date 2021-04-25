package com.example.maxdoroassigment.presentation.ui.artlist.di

import androidx.paging.ExperimentalPagingApi
import com.example.maxdoroassigment.presentation.ui.artlist.ArtListViewModelFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ExperimentalPagingApi
@Subcomponent
interface ArtListFragmentSubcomponent : AndroidInjector<ArtListViewModelFragment>{

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<ArtListViewModelFragment>
}