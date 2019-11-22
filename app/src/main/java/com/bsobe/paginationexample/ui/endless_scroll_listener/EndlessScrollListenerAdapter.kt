package com.bsobe.paginationexample.ui.endless_scroll_listener

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bsobe.paginationexample.BaseListAdapter
import com.bsobe.paginationexample.MockData
import com.bsobe.paginationexample.R

class EndlessScrollListenerAdapter(
    private val clickListener: (MockData) -> (Unit)
) : BaseListAdapter<MockData, EndlessScrollListenerAdapter.Holder>(EndlessScrollListenerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_mock_data, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            currentItem.let {
                clickListener.invoke(currentItem)
            }
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(mockData: MockData) {
            itemView.findViewById<TextView>(R.id.textViewText).text = mockData.text
            itemView.findViewById<View>(R.id.materialCardRoot).setBackgroundColor(mockData.color)
        }

    }
}