package com.bsobe.paginationexample.ui.endless_scroll_listener

import android.content.Context
import com.bsobe.paginationexample.R

data class OperationsViewState(
    val isGrid: Boolean = false
) {

    fun getLayoutManagerText(context: Context): String = if (isGrid) {
        context.getString(R.string.use_linear)
    } else {
        context.getString(R.string.use_grid)
    }

    fun createWithToggle(): OperationsViewState = copy(isGrid = !isGrid)
}