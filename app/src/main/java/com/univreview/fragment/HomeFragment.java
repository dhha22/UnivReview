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

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.RecentReview;
import com.univreview.model.Review;
import com.univreview.model.enumeration.ReviewSearchType;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.view.RecentReviewItemView;

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
        Logger.v("init");
        subjectTxt.setOnClickListener(v -> setSubjectState(isExpand));
        professorTxt.setOnClickListener(v -> Navigator.goSearch(context, ReviewSearchType.PROFESSOR, true));
        majorTxt.setOnClickListener(v -> Navigator.goMajorExpandable(context));
        collapseBtn.setOnClickListener(v -> setCollapseBtnState());

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int height = appBarLayout.getHeight() - appBarLayout.getBottom();
            float value = (float) appBarLayout.getBottom() / appBarLayout.getHeight();
            float majorValue;
            if (value == 1) {
                collapseBtn.setVisibility(View.VISIBLE);
                majorValue = 1;
                majorTxt.setVisibility(View.VISIBLE);
                collapseBtn.setAlpha(1f);
                professorTxt.setVisibility(View.VISIBLE);
            } else if (value <= 0.7) {
                majorValue = 0;
                collapseBtn.setAlpha((float) Math.log(value * 1.8));
                majorTxt.setVisibility(View.GONE);
                professorTxt.setVisibility(View.GONE);
            } else {
                majorTxt.setVisibility(View.VISIBLE);
                professorTxt.setVisibility(View.VISIBLE);
                collapseBtn.setAlpha((float) Math.log(value * 1.8));
                majorValue = (float) Math.log(value * 1.5);
            }

            if (value < 0.55) {
                collapseBtn.setVisibility(View.GONE);
            }

            collapseBtn.setAlpha((float) Math.log(value * 1.8));
            majorTxt.setAlpha(majorValue);
            professorTxt.setAlpha(majorValue);
            if (height == 0) {
                setSearchFormData(true);
            } else {
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
            setSearchFormData(true);
            this.isExpand = true;
            appBarLayout.setExpanded(true, true);
        } else {
            Logger.v("go search");
            Navigator.goSearch(context, ReviewSearchType.SUBJECT, true);
        }

    }

    private void setCollapseBtnState() {
       this. isExpand = false;
        appBarLayout.setExpanded(false, true);
        setSearchFormData(isExpand);
    }



    private void setSearchFormData(boolean isExpand) {
        Logger.v("isExpand: " + isExpand);
        if (isExpand) {
            this.isExpand = true;
            subjectTxt.setHint("과목명");
        } else {
            this.isExpand = false;
            subjectTxt.setHint("과목명·교수명·학과명");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callRecentReviewApi();
    }

    public class LatestReviewAdapter extends CustomAdapter {

        public LatestReviewAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new RecentReviewItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).v.setData((RecentReview) list.get(position));
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


        protected class ViewHolder extends RecyclerView.ViewHolder{
            final RecentReviewItemView v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = (RecentReviewItemView)itemView;
            }
        }
    }

    private void callRecentReviewApi() {
        Retro.instance.reviewService().getRecentReview(App.setAuthHeader(App.userToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> cultureResponse(result.cultures, result.majors), Logger::e,
                        () -> { cultureAdapter.clear(); majorAdapter.clear();});
    }

    private void cultureResponse(List<RecentReview> cultures, List<RecentReview> majors) {
        Observable.from(cultures)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> cultureAdapter.addItem(result), ErrorUtils::parseError);
        Observable.from(majors)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> majorAdapter.addItem(result), ErrorUtils::parseError);
    }

}
