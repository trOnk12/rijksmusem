package com.example.maxdoroassigment.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.maxdoroassigment.presentation.ui.artlist.ArtListViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BindViewModelFragment<T : ViewDataBinding, R : ViewModel>(
    private val viewModelClass: KClass<R>,
    @LayoutRes val layoutRes: Int
) : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: T

    val viewModel: R
        get() = createViewModelLazy(
            viewModelClass,
            { requireActivity().viewModelStore },
            { viewModelFactory }).value

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false) as T

        return binding.root
    }

    abstract fun initViewModel(viewModel : R)

    abstract fun initDataBinding(binding : T)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel(viewModel)
        initDataBinding(binding)
    }

}