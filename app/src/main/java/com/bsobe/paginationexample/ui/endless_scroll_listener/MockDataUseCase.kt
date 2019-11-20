package com.bsobe.paginationexample.ui.endless_scroll_listener

import com.bsobe.paginationexample.MockDataGenerator
import com.bsobe.paginationexample.MockDataResponse
import com.bsobe.paginationexample.Resource
import com.bsobe.paginationexample.Status

private const val INITIAL_ITEM_COUNT = 15
private const val ITEM_COUNT_FOR_EACH_PAGE = 10

private const val INITIAL_LOADING_TIME = 3000L
private const val PAGING_ITEM_LOADING_TIME = 2000L

class MockDataUseCase {

    private var errorPageIndex: Int = 2

    fun getPageItems(page: Int): Resource<MockDataResponse> {
        Thread.sleep(if (page == 0) INITIAL_LOADING_TIME else PAGING_ITEM_LOADING_TIME)

        if (page == errorPageIndex) {
            errorPageIndex *= 2
            return Resource(Status.ERROR, MockDataResponse(emptyList(), page), Exception())
        }

        val mockDataResponse = MockDataGenerator.generateItem(
            count = if (page == 0) INITIAL_ITEM_COUNT else ITEM_COUNT_FOR_EACH_PAGE,
            nextPage = page
        )
        return Resource.success(mockDataResponse)
    }
}