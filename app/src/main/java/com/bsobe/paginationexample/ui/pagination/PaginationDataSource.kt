package com.bsobe.paginationexample.ui.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.bsobe.paginationexample.MockData
import com.bsobe.paginationexample.MockDataGenerator
import com.bsobe.paginationexample.Status

private const val INITIAL_ITEM_COUNT = 15
private const val ITEM_COUNT_FOR_EACH_PAGE = 10

private const val INITIAL_LOADING_TIME = 3000L
private const val PAGING_ITEM_LOADING_TIME = 2000L

class PaginationDataSource : PageKeyedDataSource<Int, MockData>() {

    private var lastCallCallback: LoadCallback<Int, MockData>? = null
    private var lastCallParam: LoadParams<Int>? = null
    private var errorPageIndex: Int = 2
    private val paginationViewStateLiveData: MutableLiveData<PaginationViewState> =
        MutableLiveData()

    fun getPaginationViewStateLiveData(): MutableLiveData<PaginationViewState> =
        paginationViewStateLiveData

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MockData>
    ) {
        paginationViewStateLiveData.postValue(PaginationViewState(emptyList(), Status.LOADING))
        Thread.sleep(INITIAL_LOADING_TIME)
        val mockDataResponse = MockDataGenerator.generateItem(count = INITIAL_ITEM_COUNT)
        callback.onResult(mockDataResponse.list, null, mockDataResponse.page)
        paginationViewStateLiveData.postValue(
            PaginationViewState(
                mockDataResponse.list,
                Status.SUCCESS
            )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MockData>) {
        val viewState =
            paginationViewStateLiveData.value?.copy(status = Status.LOADING) ?: PaginationViewState(
                emptyList(),
                Status.LOADING
            )
        paginationViewStateLiveData.postValue(viewState)
        Thread.sleep(PAGING_ITEM_LOADING_TIME)

        val nextPage = params.key + 1

        if (nextPage == errorPageIndex) {
            errorPageIndex *= 2
            val errorViewState =
                paginationViewStateLiveData.value?.copy(status = Status.ERROR)
                    ?: PaginationViewState(emptyList(), Status.ERROR)
            paginationViewStateLiveData.postValue(errorViewState)
            lastCallParam = params
            lastCallCallback = callback
            return
        }

        val mockDataResponse = MockDataGenerator.generateItem(
            count = ITEM_COUNT_FOR_EACH_PAGE,
            nextPage = nextPage
        )
        callback.onResult(mockDataResponse.list, mockDataResponse.page)

        val successViewState =
            paginationViewStateLiveData.value?.copyWithPagingList(
                mockDataResponse.list,
                Status.SUCCESS
            )
                ?: PaginationViewState(mockDataResponse.list, Status.SUCCESS)
        paginationViewStateLiveData.postValue(successViewState)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MockData>) {

    }

    fun retryNextPage() {
        // Main thread blocker
        if (lastCallParam != null && lastCallCallback != null) {
            loadAfter(lastCallParam!!, lastCallCallback!!)
        }
    }
}