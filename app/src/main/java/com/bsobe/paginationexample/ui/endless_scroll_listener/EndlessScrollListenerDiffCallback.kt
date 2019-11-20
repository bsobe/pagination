package com.bsobe.paginationexample.ui.endless_scroll_listener

import androidx.recyclerview.widget.DiffUtil
import com.bsobe.paginationexample.MockData

class EndlessScrollListenerDiffCallback : DiffUtil.ItemCallback<MockData>() {

    override fun areItemsTheSame(oldItem: MockData, newItem: MockData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MockData, newItem: MockData): Boolean {
        return oldItem.color == newItem.color || oldItem.id == newItem.id
    }
}