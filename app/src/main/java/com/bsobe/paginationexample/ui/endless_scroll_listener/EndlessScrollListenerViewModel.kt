package com.bsobe.paginationexample.ui.endless_scroll_listener

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsobe.paginationexample.MockData
import com.bsobe.paginationexample.MockDataResponse
import com.bsobe.paginationexample.Resource
import com.bsobe.paginationexample.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EndlessScrollListenerViewModel : ViewModel() {

    private val mockDataUseCase: MockDataUseCase = MockDataUseCase()

    private val pageViewStateLiveData: MutableLiveData<EndlessScrollListenerViewState> =
        MutableLiveData()
    private val mockDataLiveData: MutableLiveData<List<MockData>> = MutableLiveData()
    private val mockDataResponseLiveData: MutableLiveData<MockDataResponse> = MutableLiveData()

    fun getMockDataLiveData(): LiveData<List<MockData>> = mockDataLiveData

    fun getPageViewStateLiveData(): LiveData<EndlessScrollListenerViewState> = pageViewStateLiveData


    fun loadInitial() {
        getPage(0)
    }

    fun onClickedItem(clickedItem: MockData) {
        showLoading()
        removeItems(listOf(clickedItem))
    }

    private fun removeItems(removedItems: List<MockData>) {
        viewModelScope.launch(Dispatchers.IO) {
            val removeItemResponse = mockDataUseCase.removeItemWithList(removedItems)
            val filteredList: List<MockData> = if (removeItemResponse.isStatusSuccess) {
                mockDataLiveData.value.orEmpty().filter { removedItems.contains(it).not() }
            } else {
                emptyList()
            }

            viewModelScope.launch(Dispatchers.Main) {
                mockDataLiveData.value = filteredList
                pageViewStateLiveData.value = pageViewStateLiveData.value?.copy(
                    itemList = filteredList,
                    status = Status.SUCCESS
                )
                if (filteredList.isEmpty()) {
                    mockDataResponseLiveData.value = MockDataResponse(emptyList(), 0)
                    getNextPage()
                    // TODO trigger endless scroll listener
                } else {
                    mockDataResponseLiveData.value?.let {
                        mockDataResponseLiveData.value =
                            it.copy(
                                list = filteredList,
                                page = it.page
                            )
                    }

                }
            }
        }
    }

    fun retryNextPage() {
        mockDataResponseLiveData.value?.let {
            getPage(it.page + 1)
        }
    }

    private fun getPage(page: Int) {
        showLoading()

        viewModelScope.launch(Dispatchers.IO) {
            val mockDataResponseResult: Resource<MockDataResponse> =
                mockDataUseCase.getPageItems(page)
            viewModelScope.launch(Dispatchers.Main) {
                if (mockDataResponseResult.isStatusSuccess) {
                    val mockDataResponse = mockDataResponseResult.data!!
                    mockDataLiveData.value = ArrayList<MockData>().apply {
                        addAll(mockDataLiveData.value ?: emptyList())
                        addAll(mockDataResponse.list)
                    }
                    mockDataResponseLiveData.value = mockDataResponse
                    pageViewStateLiveData.value =
                        pageViewStateLiveData.value?.copy(
                            itemList = mockDataResponse.list,
                            status = Status.SUCCESS
                        )
                } else {
                    pageViewStateLiveData.value =
                        pageViewStateLiveData.value?.copy(status = Status.ERROR)
                }
            }
        }
    }

    private fun showLoading() {
        viewModelScope.launch(Dispatchers.Main) {
            pageViewStateLiveData.value = pageViewStateLiveData.value?.copy(status = Status.LOADING)
                ?: EndlessScrollListenerViewState(emptyList(), Status.LOADING)
        }
    }

    fun getNextPage() {
        mockDataResponseLiveData.value?.let { mockDataResponse ->
            getPage(mockDataResponse.page + 1)
        }
    }

    fun remove10Item() {
        val itemList: List<MockData>? = mockDataLiveData.value
        val mockDataResponse: MockDataResponse? = mockDataResponseLiveData.value
        val pageViewState: EndlessScrollListenerViewState? = pageViewStateLiveData.value
        requireNotNull(itemList)
        requireNotNull(mockDataResponse)
        requireNotNull(pageViewState)

        /*
        val filteredList: List<MockData> = if (itemList.size > 9) {
            itemList.subList(10, itemList.lastIndex + 1)
        } else {
            emptyList()
        mockDataLiveData.value = filteredList
        mockDataResponseLiveData.value = mockDataResponse.copy(list = filteredList)
        pageViewStateLiveData.value = pageViewState.copy(itemList = filteredList)
        }
         */
        removeItems(itemList.subList(0, 10))
    }

    fun refresh() {
        mockDataLiveData.value = emptyList()
        mockDataResponseLiveData.value = null
        pageViewStateLiveData.value = null
        mockDataUseCase.reset()
        loadInitial()
    }

    override fun onCleared() {
        mockDataUseCase.reset()
        super.onCleared()
    }
}