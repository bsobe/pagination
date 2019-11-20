package com.bsobe.paginationexample

import android.graphics.Color
import android.util.Log
import java.util.*
import kotlin.random.Random

object MockDataGenerator {

    private fun nextData(): MockData {
        val text = UUID.randomUUID().toString()
        val color = Color.argb(255, generateColorHex(), generateColorHex(), generateColorHex())
        return MockData(Random.nextLong(), text, color)
    }

    private fun generateColorHex() : Int = Random.nextInt(256)

    fun generateItem(count: Int, nextPage : Int = 0): MockDataResponse {
        val mockDataList = mutableListOf<MockData>()
        for (position in 0 until count) {
            mockDataList.add(nextData())
        }
        Log.i(javaClass.simpleName, "Mock Data Generated: $count")
        Log.i(javaClass.simpleName, "Page: $nextPage")
        return MockDataResponse(mockDataList, nextPage)
    }
}