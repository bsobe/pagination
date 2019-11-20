package com.bsobe.paginationexample.ui.pagination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsobe.paginationexample.MockData
import com.bsobe.paginationexample.R
import com.bsobe.paginationexample.databinding.FragmentPaginationBinding

class PaginationFragment : Fragment() {

    private lateinit var binding: FragmentPaginationBinding
    private val paginationViewModel: PaginationViewModel by lazy {
        ViewModelProviders.of(this).get(PaginationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pagination, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paginationViewModel.pagedListLiveData.observe(this@PaginationFragment, Observer {
            onPagedListReceived(it)
        })
        paginationViewModel.pageViewStateLiveData.observe(this@PaginationFragment, Observer {
            onPageViewStateReceived(it)
        })
        setView()
    }

    private fun onPageViewStateReceived(paginationViewState: PaginationViewState?) {
        paginationViewState?.let {
            binding.viewState = it
            binding.executePendingBindings()
        }
    }

    private fun onPagedListReceived(pagedList: PagedList<MockData>?) {
        pagedList?.let {
            val paginationAdapter = binding.recyclerViewPagination.adapter as PaginationAdapter
            paginationAdapter.submitList(it)
        }
    }

    private fun setView() {
        binding.stateLayout.infoButtonListener {
            paginationViewModel.retryNextPage()
        }
        binding.recyclerViewPagination.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = PaginationAdapter(this@PaginationFragment::onClickedItem)
        }
    }

    private fun onClickedItem(clickedItem: MockData) {
        paginationViewModel.onClickedItem(clickedItem)
    }

    companion object {
        fun newInstance(): PaginationFragment = PaginationFragment()
    }
}