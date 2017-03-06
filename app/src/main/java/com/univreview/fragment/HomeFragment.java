package com.univreview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.Review;
import com.univreview.util.ErrorUtils;
import com.univreview.view.LatestReviewItemView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class HomeFragment extends BaseFragment {
    private static final boolean DEFAULT_EXPAND_STATE = false;
    @BindView(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapse_btn) ImageButton collapseBtn;
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.major_txt) TextView majorTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;
    @BindView(R.id.latest_culture_recycler_view) RecyclerView latestCultureRecyclerView;
    @BindView(R.id.latest_major_recycler_view) RecyclerView latestMajorRecyclerView;
    private LatestReviewAdapter cultureAdapter;
    private LatestReviewAdapter majorAdapter;
    private boolean isExpand = DEFAULT_EXPAND_STATE;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        subjectTxt.setOnClickListener(v -> setSubjectState(isExpand));
        professorTxt.setOnClickListener(v -> Navigator.goSearch(context, "professor", professorTxt.getText().toString(), true));
        majorTxt.setOnClickListener(v -> Navigator.goSearch(context, "major", majorTxt.getText().toString(), true));
        collapseBtn.setOnClickListener(v -> setCollapseBtnState());

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int height = appBarLayout.getHeight() - appBarLayout.getBottom();
            Logger.v("height: " + height);
            if (height == 0) {
                setSearchFormData(true);
            } else if (height >= toolbar.getHeight() * 1.4) {
                setSearchFormData(false);
            }
        });

        LinearLayoutManager cultureLayoutManager = new LinearLayoutManager(context);
        cultureLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager majorLayoutManager = new LinearLayoutManager(context);
        majorLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cultureAdapter = new LatestReviewAdapter(context);
        majorAdapter = new LatestReviewAdapter(context);
        latestCultureRecyclerView.setLayoutManager(cultureLayoutManager);
        latestMajorRecyclerView.setLayoutManager(majorLayoutManager);
        latestCultureRecyclerView.setAdapter(cultureAdapter);
        latestMajorRecyclerView.setAdapter(majorAdapter);
        latestCultureRecyclerView.setNestedScrollingEnabled(false);
        latestMajorRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setSubjectState(boolean isExpand) {
        if (!isExpand) {
            this.isExpand = true;
            appBarLayout.setExpanded(true, true);
        } else {
            Logger.v("go search");
            Navigator.goSearch(context, "subject", subjectTxt.getText().toString(), true);
        }
        setSearchFormData(isExpand);
    }

    private void setCollapseBtnState() {
       this. isExpand = false;
        appBarLayout.setExpanded(false, true);
        setSearchFormData(isExpand);
    }



    private void setSearchFormData(boolean isExpand) {
        Logger.v("isExpand: " + isExpand);
        if (isExpand) {
            collapseBtn.setVisibility(View.VISIBLE);
            majorTxt.setVisibility(View.VISIBLE);
            professorTxt.setVisibility(View.VISIBLE);
            subjectTxt.setHint("과목명");
        } else {
            collapseBtn.setVisibility(View.GONE);
            majorTxt.setVisibility(View.INVISIBLE);
            professorTxt.setVisibility(View.INVISIBLE);
            subjectTxt.setHint("과목명·교수명·학과명");
        }
    }

    public class LatestReviewAdapter extends CustomAdapter {

        public LatestReviewAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new LatestReviewItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //((ViewHolder) holder).v.setText((Review) list.get(position));
        }

        @Override
        public Review getItem(int position) {
            return (Review)list.get(position);
        }

        @Override
        public void addItem(AbstractDataProvider item) {
            list.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            final LatestReviewItemView v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = (LatestReviewItemView)itemView;
            }
        }
    }

    private void cultureResponse(List<Review> reviews){
        Observable.from(reviews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> cultureAdapter.addItem(result), ErrorUtils::parseError);
    }

    private void majorResponse(List<Review> reviews){
        Observable.from(reviews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> majorAdapter.addItem(result), ErrorUtils::parseError);
    }

}
