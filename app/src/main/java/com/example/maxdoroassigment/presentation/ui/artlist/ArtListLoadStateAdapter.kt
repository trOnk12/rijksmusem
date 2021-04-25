package com.example.maxdoroassigment.presentation.ui.artlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.maxdoroassigment.databinding.ItemLoadStateBinding

class ArtListLoadStateAdapter : LoadStateAdapter<ArtListLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLoadStateBinding.inflate(inflater)

        return LoadStateViewHolder(binding)
    }

    class LoadStateViewHolder(private val itemLoadStateBinding: ItemLoadStateBinding) :
        RecyclerView.ViewHolder(itemLoadStateBinding.root) {
        fun bind(loadState: LoadState) {
            itemLoadStateBinding.progressBar.isVisible = loadState is LoadState.Loading
        }
    }

}