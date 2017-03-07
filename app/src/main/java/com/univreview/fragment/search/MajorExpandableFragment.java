package com.univreview.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.univreview.App;
import com.univreview.R;
import com.univreview.adapter.ExpandableAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.Major;
import com.univreview.model.MajorExpandableDataProvider;
import com.univreview.network.Retro;
import com.univreview.util.Util;
import com.univreview.view.UnivReviewRecyclerView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 3. 6..
 */
public class MajorExpandableFragment extends AbsListFragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {

    private UnivReviewRecyclerView recyclerView;
    private RecyclerView.Adapter mWrappedAdapter;
    private ExpandableAdapter adapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    public static MajorExpandableFragment newInstance(){
        MajorExpandableFragment fragment = new MajorExpandableFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        init(savedInstanceState);
        toolbar.setCancelBtnVisibility(true);
        rootLayout.addView(recyclerView);
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.colorPrimary));
        return rootLayout;
    }

    private void init(Bundle savedInstanceState){
        recyclerView = new UnivReviewRecyclerView(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state to support screen rotation, etc...
        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser, Object payload) {
        Logger.v("collapse");
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser, Object payload) {
        Logger.v("expand");
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }


    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = (int) (Util.dpToPx(context, 64));
        int topMargin = (int) (Util.dpToPx(context, 16)); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

    @Override
    public UnivReviewRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void refresh() {
        setStatus(Status.REFRESHING);
        callMajorSubjectApi();
    }

    @Override
    public void loadMore() {
        Logger.v("load more");
        Logger.v("page: " + page);
        setStatus(Status.LOADING_MORE);
        callMajorSubjectApi();
    }

    private void callMajorSubjectApi(){
        Retro.instance.searchService().getMajorSubject(App.UNIVERSITY_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result.major), this::errorResponse);
    }

    private void response(List<Major> majors){
        setResult(page);
        setStatus(Status.IDLE);
        MajorExpandableDataProvider provider = new MajorExpandableDataProvider(majors);
        adapter = new ExpandableAdapter(context, provider);
        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(adapter);
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);
        recyclerView.setAdapter(mWrappedAdapter);
        recyclerView.getRecyclerView().setItemAnimator(animator);
        recyclerView.getRecyclerView().setHasFixedSize(false);
        mRecyclerViewExpandableItemManager.attachRecyclerView(recyclerView.getRecyclerView());
        adapter.setItemClickListener((view, groupPosition, childPosition) -> {
            Intent intent = new Intent();
          /*  intent.putExtra("categoryId", adapter.getChildItemId(groupPosition, childPosition));
            intent.putExtra("categoryName", adapter.getChildItemName(groupPosition, childPosition));*/
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        });
    }

    private void errorResponse(Throwable e){

    }
}
