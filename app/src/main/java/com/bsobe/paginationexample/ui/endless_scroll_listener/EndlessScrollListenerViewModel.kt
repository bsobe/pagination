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
        val filteredList = mockDataLiveData.value.orEmpty().filter { it != clickedItem }
        mockDataLiveData.value = filteredList
    }

    fun retryNextPage() {
        mockDataResponseLiveData.value?.let {
            getPage(it.page)
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
                        pageViewStateLiveData.value?.copy(itemList = mockDataResponse.list, status = Status.SUCCESS)
                } else {
                    pageViewStateLiveData.value =
                        pageViewStateLiveData.value?.copy(status = Status.ERROR)
                }
            }
        }
    }

    private fun showLoading() {
        pageViewStateLiveData.value = pageViewStateLiveData.value?.copy(status = Status.LOADING)
            ?: EndlessScrollListenerViewState(emptyList(), Status.LOADING)
    }

    fun getNextPage() {
        mockDataResponseLiveData.value?.let {
            getPage(it.page + 1)
        }
    }
}