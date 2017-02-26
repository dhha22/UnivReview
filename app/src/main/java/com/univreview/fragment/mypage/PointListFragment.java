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
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.listener.EndlessRecyclerViewScrollListener;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.PointHistory;
import com.univreview.util.Util;
import com.univreview.view.AbsRecyclerView;
import com.univreview.view.PointItemView;
import com.univreview.view.UnivReviewRecyclerView;
import com.univreview.widget.PreCachingLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 2. 17..
 */
public class PointListFragment extends AbsListFragment {
    @BindView(R.id.recycler_view) UnivReviewRecyclerView recyclerView;
    @BindView(R.id.total_point_txt) TextView totalPointTxt;
    private PointAdapter adapter;

    public static PointListFragment newInstance(){
        PointListFragment fragment = new PointListFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_point_list, container, false);
        ButterKnife.bind(this, view);
        toolbar.setBackgroundColor(Util.getColor(context, R.color.colorPrimary));
        toolbar.setBackBtnVisibility(true);
        toolbar.setTitleTxt("포인트");

        adapter = new PointAdapter(context);
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                if (getLastVisibleItemPosition() == getTotalItemCount() - 1) {
                    lastItemExposed();
                }
            }
        });
        rootLayout.addView(view);
        return rootLayout;
    }

    @Override
    public void loadMore() {
        setStatus(Status.REFRESHING);
    }

    @Override
    public void refresh() {
        Logger.v("load more");
        Logger.v("page: " + page);
        setStatus(Status.LOADING_MORE);
    }

    @Override
    public AbsRecyclerView getRecyclerView() {
        return recyclerView;
    }

    private class PointAdapter extends CustomAdapter {

        public PointAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new PointItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).v.setData((PointHistory) list.get(position));
        }



        @Override
        public AbstractDataProvider getItem(int position) {
            return null;
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            final PointItemView v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = (PointItemView) itemView;
            }
        }
    }

    private void callGetPointHistory(){

    }
}
