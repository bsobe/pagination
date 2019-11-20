package com.bsobe.paginationexample.ui.type_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bsobe.paginationexample.R
import com.bsobe.paginationexample.replaceFragment
import com.bsobe.paginationexample.ui.endless_scroll_listener.EndlessScrollListenerFragment
import com.bsobe.paginationexample.ui.pagination.PaginationFragment

class TypeSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(R.layout.fragment_type_selection, container, false)
        inflatedView.findViewById<View>(R.id.btnPagination)
            .setOnClickListener { navigateGooglePagination() }
        inflatedView.findViewById<View>(R.id.btnEndlessScrollListener)
            .setOnClickListener { navigateEndlessScrollListener() }
        return inflatedView
    }

    private fun navigateEndlessScrollListener() {
        requireActivity().replaceFragment(EndlessScrollListenerFragment.newInstance(), true)
    }

    private fun navigateGooglePagination() {
        requireActivity().replaceFragment(PaginationFragment.newInstance(), true)
    }

    companion object {
        fun newInstance(): TypeSelectionFragment = TypeSelectionFragment()
    }
}