package com.example.mahes_000.moviesapp_udacity;

import android.widget.AbsListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener
{
    // The minimum number of items to have below you current scroll position
    // before loading more
    private int visibleThreshold = 5;

    // The current offset index of data you have loaded
    private int currentPage = 0;

    // The total number of items in the dataset after the last Load
    private int previousTotalItemCount = 0;

    // True of we are still waiting for the last set of data to load.
    private boolean loading = true;

    // Sets the starting page index
    private int startingPageIndex = 0;

    public EndlessScrollListener()
    {

    }

    public EndlessScrollListener(int visibleThreshold)
    {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage)
    {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    /*  This happens many times a second during a scroll.
        We are given a few useful parameter by the onScrollListener Class
        to help us load more data as needed. (i.e. as the user scrolls through the data
    */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
            /*
                If the total item count is Zero and the previous isn't, assume the
                the list of movies / tv shows are invalid.
             */
        if (totalItemCount < previousTotalItemCount)
        {
            this.currentPage = this.startingPageIndex;

            // Here we are resetting the list back to its initial state
            this.previousTotalItemCount = totalItemCount;

            if (totalItemCount == 0)
            {
                this.loading = true; // i.e. the Data is still being loaded.
            }
        }

            /* If it is loading let's check if the data items count has changed */
        if (loading && (totalItemCount > previousTotalItemCount))
        {
            loading = false; // Stop loading the Data
            previousTotalItemCount = totalItemCount;
            currentPage++; // Updating the Current page Number
        }

            /*
                Here we check if we have crossed the visible Threshold and need to load more
                data.

                    REMEMBER: Here the data will not be loading anymore
            */
        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) > totalItemCount)
        {
            loading = onLoadMore();
        }
    }

    public abstract boolean onLoadMore();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        // Take No Action Here.
    }
}