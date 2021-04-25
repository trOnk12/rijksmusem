package com.example.maxdoroassigment.di.component

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.maxdoroassigment.presentation.di.ActivityModule
import com.example.maxdoroassigment.MyApplication
import com.example.maxdoroassigment.di.module.DataSourceModule
import com.example.maxdoroassigment.di.module.NetworkModule
import com.example.maxdoroassigment.di.module.RetrofitModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityModule::class, DataSourceModule::class, NetworkModule::class, RetrofitModule::class])
interface AppComponent {
    fun inject(application: MyApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}