package com.univreview.fragment.mypage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.univreview.fragment.MypageFragment;
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
    private PointListHeaderView headerView;

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
        recyclerView = new UnivReviewRecyclerView(getContext());
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        toolbar.setBackgroundColor(Util.getColor(getContext(), R.color.colorPrimary));
        toolbar.setBackBtnVisibility(true);
        toolbar.setTitleTxt("포인트");
        init();
        rootLayout.addView(recyclerView);
        return rootLayout;
    }

    private void init() {
        headerView = new PointListHeaderView(getContext());
        headerView.setPoint(point);
        headerView.setBuyTicketListener(v -> callBuyTicketApi());
        adapter = new PointAdapter(getContext(), headerView);
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setBackgroundColor(Util.getColor(getContext(), R.color.backgroundColor));
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
    }

    @Override
    public void loadMore() {
        Logger.v("load more");
        Logger.v("page: " + getPage());
        setStatus(Status.LOADING_MORE);
        callGetPointHistory(getPage());
    }

    @Override
    public void refresh() {
        setStatus(Status.REFRESHING);
        callGetPointHistory(getDEFAULT_PAGE());
        callGetUserTicket();
    }

    @Override
    public AbsRecyclerView getRecyclerView() {
        return recyclerView;
    }

    private class PointAdapter extends CustomAdapter {

        public PointAdapter(Context context, View headerView) {
            super(context, headerView);
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == CONTENT) {
                return new ViewHolder(new PointItemView(context));
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
             if (getItemViewType(position) == CONTENT) {
                ((ViewHolder) holder).v.setData((PointHistory) list.get(position - 1));
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
                .subscribe(result -> getUserTicketResponse(result.userTicket),this::errorResponse);

    }

    private void callGetPointHistory(int page) {
        Retro.instance.userService().getPoint(App.setAuthHeader(App.userToken), App.userId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> pointResponse(result.pointHistories, page), this::errorResponse);
    }

    private void callBuyTicketApi() {
        UserTicket userTicket = new UserTicket();
        userTicket.userId = App.userId;
        Logger.v("user ticket: " + userTicket);
        Retro.instance.userService().postUserTicket(App.setAuthHeader(App.userToken), userTicket)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    MypageFragment.isRefresh = true;
                    refresh();
                }, this::ticketErrorResponse);
    }

    private void getUserTicketResponse(List<UserTicket> userTickets) {
        if (userTickets.size() > 0) {
            headerView.setUserTicket(userTickets.get(0));
        } else {
            headerView.setUserTicket(null);
        }
    }

    private void pointResponse(List<PointHistory> pointHistories, int page) {
        Logger.v("result: " + pointHistories);
        if (page == getDEFAULT_PAGE()) adapter.clear();
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

    private void ticketErrorResponse(Throwable e) {
        String message = ErrorUtils.getErrorBody(e);
        if (ErrorUtils.parseError(e) == ErrorUtils.ERROR_400) {
            new AlertDialog.Builder(getContext(), R.style.customDialog)
                    .setMessage(message)
                    .setPositiveButton("확인", null)
                    .show();
        }
    }

}
