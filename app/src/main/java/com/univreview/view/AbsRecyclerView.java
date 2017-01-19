package com.univreview.view;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public interface AbsRecyclerView {
    void onRefreshComplete();
    boolean post(Runnable r);
    void setRefreshing(boolean b);
    void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener);
    boolean isRefreshing();
}
