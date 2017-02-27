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

import com.univreview.App;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.listener.EndlessRecyclerViewScrollListener;
import com.univreview.log.Logger;
import com.univreview.model.PointHistory;
import com.univreview.model.UserTicket;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.Util;
import com.univreview.view.AbsRecyclerView;
import com.univreview.view.PointItemView;
import com.univreview.view.PointListHeaderView;
import com.univreview.view.UnivReviewRecyclerView;
import com.univreview.widget.PreCachingLayoutManager;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 2. 17..
 */
public class PointListFragment extends AbsListFragment {
    private UnivReviewRecyclerView recyclerView;
    private int point;
    private PointAdapter adapter;

    public static PointListFragment newInstance(int point){
        PointListFragment fragment = new PointListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("point", point);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        point = getArguments().getInt("point");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        recyclerView = new UnivReviewRecyclerView(context);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        toolbar.setBackgroundColor(Util.getColor(context, R.color.colorPrimary));
        toolbar.setBackBtnVisibility(true);
        toolbar.setTitleTxt("포인트");

        adapter = new PointAdapter(context);
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setBackgroundColor(Util.getColor(context, R.color.backgroundColor));
        recyclerView.setLayoutManager(layoutManager);
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
        rootLayout.addView(recyclerView);
        return rootLayout;
    }

    @Override
    public void loadMore() {
        Logger.v("load more");
        Logger.v("page: " + page);
        setStatus(Status.LOADING_MORE);
        callGetPointHistory(page);
    }

    @Override
    public void refresh() {
        setStatus(Status.REFRESHING);
        callGetPointHistory(DEFAULT_PAGE);
        callGetUserTicket();
    }

    @Override
    public AbsRecyclerView getRecyclerView() {
        return recyclerView;
    }

    private class PointAdapter extends CustomAdapter {
        private static final int HEADER = 0;
        private static final int CONTENT = 1;
        private UserTicket userTicket;

        public PointAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER) {
                return new HeaderViewHolder(new PointListHeaderView(context));
            }
            return new ViewHolder(new PointItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position) == HEADER){
                ((HeaderViewHolder) holder).v.setUserTicket(userTicket);
            }else {
                ((ViewHolder) holder).v.setData((PointHistory) list.get(position));
            }
        }


        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER;
            }
            return CONTENT;
        }

        public void setUserTicket(UserTicket userTicket){
            this.userTicket = userTicket;
            notifyDataSetChanged();
        }


        protected class HeaderViewHolder extends RecyclerView.ViewHolder{
            final PointListHeaderView v;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                v = (PointListHeaderView) itemView;
                v.setPoint(point);
            }
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            final PointItemView v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = (PointItemView) itemView;
            }
        }
    }


    //api
    private void callGetUserTicket(){
        Retro.instance.userService().getUserTicket(App.setAuthHeader(App.userToken), App.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> ticketResponse(result.userTicket));

    }

    private void callGetPointHistory(int page) {
        Retro.instance.userService().getPoint(App.setAuthHeader(App.userToken), App.userId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> pointResponse(result.pointHistories), this::errorResponse);
    }

    private void ticketResponse(List<UserTicket> userTickets) {
        if(userTickets.size()>0) {
            adapter.setUserTicket(userTickets.get(0));
        }else{
            adapter.setUserTicket(null);
        }
    }

    private void pointResponse(List<PointHistory> pointHistories) {
        Logger.v("result: " + pointHistories);
        setResult(page);
        setStatus(Status.IDLE);
        Observable.from(pointHistories)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> adapter.addItem(result), Logger::e);
    }

    private void errorResponse(Throwable e) {
        setStatus(Status.ERROR);
        ErrorUtils.parseError(e);
    }
}
