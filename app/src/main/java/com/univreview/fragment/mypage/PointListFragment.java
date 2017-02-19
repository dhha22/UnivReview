package com.univreview.fragment.mypage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.model.AbstractDataProvider;
import com.univreview.util.Util;
import com.univreview.view.AbsRecyclerView;
import com.univreview.view.PointItemView;
import com.univreview.view.UnivReviewRecyclerView;

/**
 * Created by DavidHa on 2017. 2. 17..
 */
public class PointListFragment extends AbsListFragment {
    private UnivReviewRecyclerView recyclerView;
    private PointAdapter adapter;

    public static PointListFragment newInstance(){
        PointListFragment fragment = new PointListFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        toolbar.setBackgroundColor(Util.getColor(context, R.color.colorPrimary));
        toolbar.setBackBtnVisibility(true);
        recyclerView = new UnivReviewRecyclerView(context);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new PointAdapter(context);
        recyclerView.setAdapter(adapter);
        rootLayout.addView(recyclerView);
        return rootLayout;
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public AbsRecyclerView getRecyclerView() {
        return recyclerView;
    }

    private class PointAdapter extends CustomAdapter{

        public PointAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new PointItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder)holder).v.setData();
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        @Override
        public void addItem(AbstractDataProvider item) {

        }

        @Override
        public AbstractDataProvider getItem(int position) {
            return null;
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            final PointItemView v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = (PointItemView)itemView;
            }
        }
    }
}
