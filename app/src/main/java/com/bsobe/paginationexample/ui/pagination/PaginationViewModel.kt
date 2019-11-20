package com.bsobe.paginationexample.ui.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.bsobe.paginationexample.MockData

class PaginationViewModel : ViewModel() {

    private lateinit var paginationDataSource: PaginationDataSource

    val pagedListLiveData: LiveData<PagedList<MockData>> by lazy {
        val config: PagedList.Config = PagedList.Config.Builder()
            .setPageSize(4)
            .setEnablePlaceholders(false)
            .build()
        initializedPagedListBuilder(config).build()
    }

    lateinit var pageViewStateLiveData: LiveData<PaginationViewState>

    private fun initializedPagedListBuilder(config: PagedList.Config)
        : LivePagedListBuilder<Int, MockData> {

        paginationDataSource = PaginationDataSource()
        val dataSourceFactory: DataSource.Factory<Int, MockData> =
            object : DataSource.Factory<Int, MockData>() {
                override fun create(): DataSource<Int, MockData> {
                    return paginationDataSource
                }
            }
        pageViewStateLiveData = paginationDataSource.getPaginationViewStateLiveData()
        return LivePagedListBuilder<Int, MockData>(dataSourceFactory, config)
    }

    fun retryNextPage() {
        paginationDataSource.retryNextPage()
    }

    fun onClickedItem(clickedItem: MockData) {
        val filteredList = paginationDataSource.getPaginationViewStateLiveData()
            .value?.itemList?.filter { it != clickedItem }.orEmpty()
        paginationDataSource.getPaginationViewStateLiveData().value =
            paginationDataSource.getPaginationViewStateLiveData()
                .value?.copy(itemList = filteredList)
        paginationDataSource.invalidate()
        paginationDataSource.invalidate()
    }

}