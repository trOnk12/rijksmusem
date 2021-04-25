package com.example.maxdoroassigment.presentation.ui.artlist.di

import androidx.paging.ExperimentalPagingApi
import com.example.maxdoroassigment.presentation.ui.artlist.ArtComparator
import com.example.maxdoroassigment.presentation.ui.artlist.ArtListAdapter
import com.example.maxdoroassigment.presentation.ui.artlist.ArtListViewModelFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@ExperimentalPagingApi
@Module(subcomponents = [ArtListFragmentSubcomponent::class])
abstract class ArtListFragmentModule {
    companion object {

        @Provides
        fun provideArtListAdapter(): ArtListAdapter {
            return ArtListAdapter(ArtComparator)
        }

    }

    @Binds
    @IntoMap
    @ClassKey(ArtListViewModelFragment::class)
    abstract fun bindYourFragmentInjectorFactory(factory: ArtListFragmentSubcomponent.Factory): AndroidInjector.Factory<*>

}