package com.univreview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.univreview.log.Logger;
import com.univreview.view.AbsRecyclerView;
import com.univreview.view.UnivReviewRecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public abstract class AbsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    protected static final int DEFAULT_PAGE = 1;
    private static final Map<Class, Boolean> needRefresh = new HashMap<>();
    protected ProgressBar loadMoreProgress;
    public int page = DEFAULT_PAGE;

    public static void setNeedRefresh(Class clazz, boolean refresh) {
        needRefresh.put(clazz, refresh);
    }

    public boolean isNeedRefresh() {
        final Class<?> aClass = ((Object) this).getClass();
        if (needRefresh.containsKey(aClass)) {
            return needRefresh.get(aClass);
        }
        return true;
    }

    public enum Status {
        IDLE, LOADING_MORE, REFRESHING, ERROR
    }

    private Status status = Status.LOADING_MORE;

    public void setResult(int page) {
        this.page = page + 1;
        Logger.v("set result page: " + page);
    }

    public void lastItemExposed() {
        if (getStatus() != Status.LOADING_MORE && getStatus() != Status.REFRESHING) {
            loadMore();
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        Logger.i("status " + status);
        if (this.status == status) {
            return;
        }
        this.status = status;
        final AbsRecyclerView recyclerView;
        if ((recyclerView = getRecyclerView()) != null) {
            switch (status) {
                case IDLE:
                    if (loadMoreProgress != null) {
                        loadMoreProgress.setVisibility(View.GONE);
                    }
                    recyclerView.onRefreshComplete();
                    break;
                case LOADING_MORE:
                    if (loadMoreProgress != null) {
                        loadMoreProgress.setVisibility(View.VISIBLE);
                    }
                    break;
                case REFRESHING:
                    if (loadMoreProgress != null) {
                        loadMoreProgress.setVisibility(View.GONE);
                    }
                    recyclerView.setRefreshing(true);
                    break;
                case ERROR:
                    if (loadMoreProgress != null) {
                        loadMoreProgress.setVisibility(View.GONE);
                    }
                    recyclerView.onRefreshComplete();
                    break;
            }
        }
    }

    abstract public AbsRecyclerView getRecyclerView();

    abstract public void loadMore();

    abstract public void refresh();

    @Override
    public void onResume() {
        super.onResume();
        if (isNeedRefresh()) {
            setNeedRefresh(((Object) this).getClass(), false);
            if (status != Status.REFRESHING) {
                refresh();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getRecyclerView() != null) {
            getRecyclerView().setOnRefreshListener(this);
            Logger.i("setOnRefreshListener " + ((Object) this).getClass().getSimpleName());
        }
    }


    @Override
    public void onRefresh() {
        if (status != Status.REFRESHING) {
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getRecyclerView() != null) {
            if (getRecyclerView() instanceof UnivReviewRecyclerView) {
                ((UnivReviewRecyclerView) getRecyclerView()).removeAllViews();
                ((UnivReviewRecyclerView) getRecyclerView()).setAdapter(null);
            } else if (getRecyclerView() instanceof RecyclerView) {
                ((RecyclerView) getRecyclerView()).removeAllViews();
                ((RecyclerView) getRecyclerView()).setAdapter(null);
            }
        }
        setNeedRefresh(((Object) this).getClass(), true);
    }
}
