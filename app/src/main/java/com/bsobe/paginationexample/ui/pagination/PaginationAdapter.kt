package com.bsobe.paginationexample.ui.pagination

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bsobe.paginationexample.MockData
import com.bsobe.paginationexample.R

class PaginationAdapter(private val clickListener: (MockData) -> (Unit)) :
    PagedListAdapter<MockData, PaginationAdapter.Holder>(PaginationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_mock_data, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            currentItem?.let {
                clickListener.invoke(currentItem)
            }
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(mockData: MockData?) {
            itemView.findViewById<TextView>(R.id.textViewText).text = mockData?.text.orEmpty()
            itemView.findViewById<View>(R.id.frameLayoutRoot)
                .setBackgroundColor(mockData?.color ?: Color.BLACK)
        }

    }
}