package com.bsobe.paginationexample

import android.graphics.Color
import kotlin.random.Random

object PagingDataGenerator {

    private var index = 0

    private fun nextData(): MockData {
        //val text = UUID.randomUUID().toString()
        val text = (++index).toString()
        val color = Color.argb(255, generateColorHex(), generateColorHex(), generateColorHex())
        return MockData(Random.nextLong(), text, color)
    }

    private fun generateColorHex(): Int = Random.nextInt(256)

    @Synchronized
    fun getPage(pageIndex: Int, itemCountForPage: Int): MockDataResponse {
        generateMockData(itemCountForPage)
        val startIndex = pageIndex * itemCountForPage
        val mockDataList = pagingDataList.subList(startIndex, startIndex + itemCountForPage)

        return MockDataResponse(mockDataList, pageIndex)
    }

    private fun generateMockData(itemCountForPage: Int) {
        val mockDataList = mutableListOf<MockData>()
        for (position in 0 until 2 * itemCountForPage) {
            mockDataList.add(nextData())
        }
        pagingDataList.addAll(mockDataList)
    }

    @Synchronized
    fun removeItem(removeItem: MockData) {
        pagingDataList.remove(removeItem)
    }

    val pagingDataList: MutableList<MockData> = mutableListOf()

    fun reset(){
        pagingDataList.clear()
        index = 0
    }
}