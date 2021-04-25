package com.example.maxdoroassigment.presentation.di

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.maxdoroassigment.presentation.ui.artlist.ArtListViewModel
import com.example.maxdoroassigment.di.module.viewmodel.ViewModelBuilder
import com.example.maxdoroassigment.di.module.viewmodel.ViewModelKey
import com.example.maxdoroassigment.di.scope.ActivityScope
import com.example.maxdoroassigment.presentation.MainActivity
import com.example.maxdoroassigment.presentation.ui.artlist.di.ArtListFragmentModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@ExperimentalPagingApi
@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ViewModelBuilder::class,ArtListFragmentModule::class])
    abstract fun contributeYourAndroidInjector(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(ArtListViewModel::class)
    abstract fun bindViewModel(viewmodel: ArtListViewModel): ViewModel

}