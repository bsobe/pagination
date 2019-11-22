package com.bsobe.paginationexample.ui.endless_scroll_listener

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bsobe.paginationexample.ItemChangePayload

class RecyclerViewEndlessScrollListener : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading: Boolean = true
    private var visibleThreshold: Int = 4
    private var currentItemCount: Int = 0
    private var currentPage: Int = 1

    var pageCount: Int = 0
    lateinit var loadListener : (Int) -> Unit

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager: RecyclerView.LayoutManager = recyclerView.layoutManager ?: return
        currentItemCount = layoutManager.itemCount

        if (currentItemCount < previousTotal) {
            reset()
        }

        if (loading && isTotalItemCountRecentlyIncreased()) {
            loading = false
            previousTotal = currentItemCount
        }

        if (shouldLoadNextPage(layoutManager, visibleThreshold)) {
            currentPage++
            recyclerView.post { loadListener.invoke(currentPage) }
            loading = true
        }
    }

    private fun isTotalItemCountRecentlyIncreased(): Boolean =
        currentItemCount > previousTotal + visibleThreshold

    private fun shouldLoadNextPage(
        layoutManager: RecyclerView.LayoutManager,
        threshold: Int
    ): Boolean {
        return !loading
            && isLastVisibleItemPositionExceedsTotalItemCount(layoutManager, threshold)
            && !isLastPage()
    }

    private fun isLastVisibleItemPositionExceedsTotalItemCount(
        layoutManager: RecyclerView.LayoutManager,
        threshold: Int
    ): Boolean {
        return if (layoutManager is LinearLayoutManager) {
            layoutManager.findLastVisibleItemPosition() + threshold >= currentItemCount
        } else if (layoutManager is GridLayoutManager) {
            layoutManager.findLastVisibleItemPosition() + threshold >= currentItemCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val findLastVisibleItemPositions =
                layoutManager.findLastVisibleItemPositions(null)
            var isExceedsTotalItemCount = false
            for (position: Int in findLastVisibleItemPositions) {
                isExceedsTotalItemCount = position + threshold >= currentItemCount
                if (isExceedsTotalItemCount) {
                    break
                }
            }
            isExceedsTotalItemCount
        } else false
    }

    private fun isLastPage(): Boolean = currentPage == pageCount

    private fun reset() {
        previousTotal = 0
        loading = true
        currentPage = 1
    }

    fun onDataChange(changePayload: ItemChangePayload) {
        if (changePayload.changeType == ItemChangePayload.ChangeType.REMOVE) {
            previousTotal -= 1
        }
        // else can be added in need
    }
}