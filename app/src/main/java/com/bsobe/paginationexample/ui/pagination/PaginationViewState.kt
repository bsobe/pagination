package com.bsobe.paginationexample.ui.pagination

import android.content.Context
import com.bsobe.paginationexample.MockData
import com.bsobe.paginationexample.R
import com.bsobe.paginationexample.Status
import com.erkutaras.statelayout.StateLayout

data class PaginationViewState(
    val itemList: List<MockData>,
    val status: Status
) {

    fun getState(context: Context): StateLayout.StateInfo {
        return when (status) {
            Status.LOADING -> if (itemList.isEmpty()) StateLayout.provideLoadingStateInfo() else StateLayout.provideLoadingWithContentStateInfo()
            Status.SUCCESS -> StateLayout.provideContentStateInfo()
            Status.ERROR -> provideErrorEmptyState(context)
        }
    }

    private fun provideErrorEmptyState(context: Context): StateLayout.StateInfo =
        StateLayout.StateInfo(
            R.drawable.ic_launcher_background,
            context.getString(R.string.pagination_error_title),
            context.getString(R.string.pagination_error_message),
            context.getString(R.string.pagination_try_again),
            StateLayout.State.EMPTY
        )

    fun copyWithPagingList(
        itemList: List<MockData>? = null,
        status: Status = this.status
    ): PaginationViewState? {
        val newList = mutableListOf<MockData>().apply {
            addAll(itemList ?: emptyList())
            addAll(this@PaginationViewState.itemList)
        }
        return PaginationViewState(newList, status)
    }
}