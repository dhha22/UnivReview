package com.univreview.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ProgressBar;

import com.univreview.view.AbsRecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public abstract class AbsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final Map<Class, Boolean> needRefresh = new HashMap<>();
    protected ProgressBar loadMoreProgress;
    public int page;

    public enum Status {
        IDLE, LOADING_MORE, REFRESHING, ERROR
    }

    private Status status = Status.LOADING_MORE;

    public void setResult(int page) {
        this.page = page + 1;
    }

    public void lastItemExposed() {
        if (getStatus() != Status.LOADING_MORE ) {
            loadMore();
        }
    }

    public Status getStatus() {
        return status;
    }

    abstract public AbsRecyclerView getRecyclerView();

    abstract public void loadMore();

    abstract public void refresh();
}
