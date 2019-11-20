package com.bsobe.paginationexample

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T, VH : RecyclerView.ViewHolder>
constructor(diffCallback: DiffUtil.ItemCallback<T>) : RecyclerView.Adapter<VH>() {

    private val diffHelper = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(diffCallback)
            .build())

    init {
        submitList(mutableListOf())
    }

    private fun getMutableList(): MutableList<T> {
        return mutableListOf<T>().apply {
            addAll(diffHelper.currentList)
        }
    }

    override fun getItemCount(): Int {
        return diffHelper.currentList.size
    }

    fun submitList(list: List<T>) {
        diffHelper.submitList(list)
    }

    @JvmOverloads
    fun submitItem(position: Int = itemCount, item: T) {
        val newList = getMutableList()
        newList.add(position, item)
        submitList(newList)
    }

    fun clearItems() {
        submitList(mutableListOf())
    }

    fun removeItem(index: Int) {
        val newList = getMutableList()
        newList.removeAt(index)
        submitList(newList)
    }

    fun removeItem(item: T): Boolean {
        val newList = getMutableList()
        val result = newList.remove(item)
        submitList(newList)
        return result
    }

    fun getItems(): MutableList<T> = diffHelper.currentList

    protected fun getItem(position: Int): T = getItems().get(position)
}
