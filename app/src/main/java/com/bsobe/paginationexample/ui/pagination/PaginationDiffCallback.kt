package com.bsobe.paginationexample.ui.pagination

import androidx.recyclerview.widget.DiffUtil
import com.bsobe.paginationexample.MockData

class PaginationDiffCallback : DiffUtil.ItemCallback<MockData>() {

    override fun areItemsTheSame(oldItem: MockData, newItem: MockData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MockData, newItem: MockData): Boolean {
        return oldItem.color == newItem.color || oldItem.id == newItem.id
    }
}