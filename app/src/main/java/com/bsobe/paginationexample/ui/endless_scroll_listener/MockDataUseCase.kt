package com.bsobe.paginationexample.ui.endless_scroll_listener

import com.bsobe.paginationexample.MockData
import com.bsobe.paginationexample.MockDataResponse
import com.bsobe.paginationexample.PagingDataGenerator
import com.bsobe.paginationexample.Resource
import com.bsobe.paginationexample.Status

private const val REMOVE_LOADING_TIME = 400L
private const val INITIAL_LOADING_TIME = 300L
private const val PAGING_ITEM_LOADING_TIME = 200L
private const val ITEM_COUNT_FOR_PAGE = 10

class MockDataUseCase {

    private var pendingRemovedItemCount: Int = 0
    private var errorPageIndex: Int = 2

    fun reset() {
        PagingDataGenerator.reset()
        errorPageIndex = 2
    }

    fun getPageItems(page: Int): Resource<MockDataResponse> {
        Thread.sleep(if (page == 0) INITIAL_LOADING_TIME else PAGING_ITEM_LOADING_TIME)

        if (page == errorPageIndex) {
            errorPageIndex *= 2
            return Resource(Status.ERROR, MockDataResponse(emptyList(), page), Exception())
        }

        val mockDataResponse = PagingDataGenerator.getPage(page, ITEM_COUNT_FOR_PAGE)
        return Resource.success(mockDataResponse)
    }

    fun removeItem(removeItem: MockData): Resource<Boolean> {
        Thread.sleep(REMOVE_LOADING_TIME)
        PagingDataGenerator.removeItem(removeItem)
        return Resource.success(true)
    }

    fun removeItemWithList(itemList: List<MockData>): Resource<Boolean> {
        Thread.sleep(REMOVE_LOADING_TIME)
        itemList.forEach {
            PagingDataGenerator.removeItem(it)
        }
        pendingRemovedItemCount += itemList.size
        return Resource.success(true)
    }

    private fun getPendingPageCount(): Int =
        if (pendingRemovedItemCount % ITEM_COUNT_FOR_PAGE == 0) {
            pendingRemovedItemCount / ITEM_COUNT_FOR_PAGE
        } else {
            (pendingRemovedItemCount / ITEM_COUNT_FOR_PAGE) + 1
        }

    fun getPendingPageIndex(currentPageIndex: Int): Int {
        val index = currentPageIndex - getPendingPageCount()
        return if (index < 0) {
            0
        } else {
            index
        }
    }
}