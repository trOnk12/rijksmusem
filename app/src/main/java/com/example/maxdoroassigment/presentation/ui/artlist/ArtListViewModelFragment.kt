package com.example.maxdoroassigment.presentation.ui.artlist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.maxdoroassigment.R
import com.example.maxdoroassigment.core.base.BindViewModelFragment
import com.example.maxdoroassigment.core.extension.actionOnRefresh
import com.example.maxdoroassigment.core.extension.observe
import com.example.maxdoroassigment.databinding.FragmentArtListBinding
import com.example.maxdoroassigment.presentation.model.ArtListItem
import javax.inject.Inject

@ExperimentalPagingApi
class ArtListViewModelFragment : BindViewModelFragment<FragmentArtListBinding, ArtListViewModel>(
    viewModelClass = ArtListViewModel::class,
    layoutRes = R.layout.fragment_art_list
) {

    @Inject
    lateinit var itemAdapter: ArtListAdapter

    override fun initViewModel(viewModel: ArtListViewModel) {
        with(viewModel) {
            observe(artListItemPagingData, ::onPagingDataChanged)
        }
    }

    override fun initDataBinding(binding: FragmentArtListBinding) {
        with(binding) {
            with(artList) {
                adapter = itemAdapter.withLoadStateHeaderAndFooter(
                    header = ArtListLoadStateAdapter(),
                    footer = ArtListLoadStateAdapter()
                )
            }

            with(swiperefresh) {
                actionOnRefresh { itemAdapter.refresh() }
            }
        }
    }

    private fun onPagingDataChanged(pagingData: PagingData<ArtListItem>) {
        itemAdapter.submitData(lifecycle, pagingData)
    }


}