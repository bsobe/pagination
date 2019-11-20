package com.bsobe.paginationexample.ui.endless_scroll_listener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsobe.paginationexample.MockData
import com.bsobe.paginationexample.R
import com.bsobe.paginationexample.RecyclerViewEndlessScrollListener
import com.bsobe.paginationexample.databinding.FragmentEndlessScrollListenerBinding

class EndlessScrollListenerFragment : Fragment() {

    private lateinit var binding: FragmentEndlessScrollListenerBinding
    private val endlessScrollListenerViewModel: EndlessScrollListenerViewModel by lazy {
        ViewModelProviders.of(this).get(EndlessScrollListenerViewModel::class.java)
    }

    private val endlessScrollListener: RecyclerViewEndlessScrollListener by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        object :
            RecyclerViewEndlessScrollListener(binding.recyclerViewEndlessScrollListener.layoutManager as LinearLayoutManager) {
            override fun onLoadNextPage(page: Int) {
                endlessScrollListenerViewModel.getNextPage()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_endless_scroll_listener,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endlessScrollListenerViewModel.getMockDataLiveData()
            .observe(this@EndlessScrollListenerFragment, Observer {
                onMockDataReceived(it)
            })
        endlessScrollListenerViewModel.getPageViewStateLiveData()
            .observe(this@EndlessScrollListenerFragment, Observer {
                onPageViewStateReceived(it)
            })
        setView()
        endlessScrollListenerViewModel.loadInitial()
    }

    private fun setView() {
        binding.stateLayout.infoButtonListener {
            endlessScrollListenerViewModel.retryNextPage()
        }
        binding.recyclerViewEndlessScrollListener.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = EndlessScrollListenerAdapter(
                this@EndlessScrollListenerFragment::onClickedItem
            )
            addOnScrollListener(endlessScrollListener)
        }
    }

    private fun onClickedItem(clickedItem: MockData) {
        endlessScrollListenerViewModel.onClickedItem(clickedItem)
    }

    private fun onPageViewStateReceived(viewState: EndlessScrollListenerViewState?) {
        binding.viewState = viewState
        binding.executePendingBindings()
    }

    private fun onMockDataReceived(itemList: List<MockData>) {
        with(binding) {
            val adapter = recyclerViewEndlessScrollListener.adapter as EndlessScrollListenerAdapter
            adapter.submitList(itemList)
        }
    }

    companion object {
        fun newInstance(): EndlessScrollListenerFragment = EndlessScrollListenerFragment()
    }
}