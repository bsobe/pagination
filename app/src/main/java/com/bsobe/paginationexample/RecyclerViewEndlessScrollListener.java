package com.bsobe.paginationexample;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsobe.paginationexample.ItemChangePayload;

public abstract class RecyclerViewEndlessScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4;
    private int currentItemCount;
    private int currentPage = 1;
    private LinearLayoutManager linearLayoutManager;
    @Nullable
    private Integer pageCount;

    public RecyclerViewEndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        currentItemCount = linearLayoutManager.getItemCount();

        if (currentItemCount < previousTotal) {
            reset();
        }

        if (loading && isTotalItemCountRecentlyIncreased()) {
            loading = false;
            previousTotal = currentItemCount;
        }

        if (shouldLoadNextPage(visibleThreshold)) {
            currentPage++;
            recyclerView.post(()->onLoadNextPage(currentPage));
            loading = true;
        }
    }

    private boolean isTotalItemCountRecentlyIncreased() {
        return currentItemCount > previousTotal + visibleThreshold;
    }

    private boolean shouldLoadNextPage(int threshold) {
        return !loading && isLastVisibleItemPositionExceedsTotalItemCount(threshold) && !isLastPage();
    }

    private boolean isLastVisibleItemPositionExceedsTotalItemCount(int threshold) {
        return linearLayoutManager.findLastVisibleItemPosition() + threshold >= currentItemCount;
    }

    private boolean isLastPage() {
        return pageCount != null && currentPage == pageCount;
    }

    private void reset() {
        previousTotal = 0;
        loading = true;
        currentPage = 1;
    }

    public abstract void onLoadNextPage(int page);

    public void onDataChange(ItemChangePayload changePayload) {
        if (changePayload.getChangeType() == ItemChangePayload.ChangeType.REMOVE) {
            previousTotal -= 1;
        }
        // else can be added in need
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}