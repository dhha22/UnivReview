package com.univreview.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.univreview.R;
import com.univreview.fragment.AbsListFragment;

/**
 * Created by DavidHa on 2017. 1. 21..
 */
public class UnivReviewRecyclerView extends SwipeRefreshLayout implements AbsRecyclerView{
    private RecyclerViewCustom recyclerview;

    public UnivReviewRecyclerView(Context context) {
        this(context, null);
    }

    public UnivReviewRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent);
        recyclerview = (RecyclerViewCustom) LayoutInflater.from(context).inflate(R.layout.recyclerview, null);
        addView(recyclerview);
    }

    @Override
    public void onRefreshComplete() {
        setRefreshing(false);
    }

    public RecyclerView getRecyclerView() {
        return recyclerview;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        recyclerview.setLayoutManager(layoutManager);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener scrollListener){
        recyclerview.addOnScrollListener(scrollListener);
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        this.recyclerview.setAdapter(adapter);
    }

    public void scrollToTop(){
        recyclerview.scrollToPosition(0);
    }




    public enum Mode {
        DISABLED, ENABLED
    }

    public void setMode(Mode mode) {
        switch (mode) {
            case DISABLED:
                setEnabled(false);
                break;

            default:
                setEnabled(true);
        }
    }
}

