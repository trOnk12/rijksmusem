package com.example.maxdoroassigment.presentation.ui.artlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.maxdoroassigment.databinding.ItemArtPieceBinding
import com.example.maxdoroassigment.presentation.model.ArtListItem

class ArtListAdapter(differCallback: DiffUtil.ItemCallback<ArtListItem>) :
    PagingDataAdapter<ArtListItem, RecyclerView.ViewHolder>(differCallback) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewHolder = holder as ArtViewHolder

        if (item != null) {
            viewHolder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArtPieceBinding.inflate(inflater)

        return ArtViewHolder(binding)
    }

    class ArtViewHolder(private val binding: ItemArtPieceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(artListItem: ArtListItem) {
            binding.artListItem = artListItem
            binding.executePendingBindings()
        }
    }
}

object ArtComparator : DiffUtil.ItemCallback<ArtListItem>() {
    override fun areItemsTheSame(oldItem: ArtListItem, newItem: ArtListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArtListItem, newItem: ArtListItem): Boolean {
        return oldItem == newItem
    }
}


