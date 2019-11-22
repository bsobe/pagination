package com.bsobe.paginationexample.ui.endless_scroll_listener

import android.content.Context
import androidx.databinding.DataBindingUtil
import com.bsobe.paginationexample.R
import com.bsobe.paginationexample.databinding.DialogOperationBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class OperationsBottomSheetDialog(context: Context) : BottomSheetDialog(context) {

    private var binding: DialogOperationBinding = DataBindingUtil.inflate(
        layoutInflater,
        R.layout.dialog_operation,
        null,
        false
    )

    fun setListener(
        remove10Listener: () -> Unit,
        refreshListener: () -> Unit,
        layoutManagerListener: (Boolean) -> Unit
    ): OperationsBottomSheetDialog {
        binding.buttonRemove10.setOnClickListener {
            remove10Listener.invoke()
        }
        binding.buttonRefresh.setOnClickListener { refreshListener.invoke() }
        binding.buttonChangeLayout.setOnClickListener {
            binding.viewState = binding.viewState?.createWithToggle()
            binding.executePendingBindings()
            layoutManagerListener.invoke(binding.viewState?.isGrid ?: false)
        }
        return this
    }

    init {
        setContentView(binding.root)
        binding.viewState = OperationsViewState(false)
        binding.executePendingBindings()
    }
}