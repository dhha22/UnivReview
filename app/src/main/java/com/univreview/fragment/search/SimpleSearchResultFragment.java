package com.univreview.fragment.search;

import android.content.Context;
import android.content.Intent;
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
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.SearchModel;
import com.univreview.network.Retro;
import com.univreview.util.Util;
import com.univreview.view.AbsRecyclerView;
import com.univreview.view.SearchListItemView;
import com.univreview.view.UnivReviewRecyclerView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 3. 6..
 */
public class SimpleSearchResultFragment extends BaseFragment {
    private static final String PROFESSOR = "professor";
    private static final String SUBJECT = "subject";
    private UnivReviewRecyclerView recyclerView;
    private SearchAdapter adapter;
    private long id;
    private String type;

    public static SimpleSearchResultFragment newInstance(String type, long id){
        SimpleSearchResultFragment fragment = new SimpleSearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getLong("id");
        type = getArguments().getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        toolbar.setCancelBtnVisibility(true);
        init();
        rootLayout.addView(recyclerView);
        rootLayout.setBackgroundColor(Util.getColor(context, R.color.searchBgColor));
        return rootLayout;
    }

    private void init(){
        recyclerView = new UnivReviewRecyclerView(context);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setMode(UnivReviewRecyclerView.Mode.DISABLED);
        recyclerView.setAdapter(adapter);
        if(type.equals(PROFESSOR)){
            callGetProfessorSubject(id);
        }else if(type.equals(SUBJECT)){
            callGetSubjectProfessor(id);
        }
        adapter = new SearchAdapter(context);
        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent();
            Logger.v("id : " + adapter.getItem(position).getId());
            Logger.v("type : " + type);
            intent.putExtra("id", adapter.getItem(position).getId());
            intent.putExtra("name", adapter.getItem(position).getName());
            intent.putExtra("type", type);
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().onBackPressed();
        });

    }


    private class SearchAdapter extends CustomAdapter{

        public SearchAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new SearchListItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder)holder).v.setText(list.get(position).getName());
        }


        protected class ViewHolder extends RecyclerView.ViewHolder{
            final SearchListItemView v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = (SearchListItemView)itemView;
                v.setTextSize(24);
                v.setTextCenter(true);
                v.setOnClickListener(v -> itemClickListener.onItemClick(v, getAdapterPosition()));
            }
        }

    }

    private void callGetProfessorSubject(long id){
        Retro.instance.searchService().getProfessorSubject(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result, "subject"), Logger::e);
    }

    private void callGetSubjectProfessor(long id){
        Retro.instance.searchService().getSubjectProfessor(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result, "professor"), Logger::e);
    }

    private void response(SearchModel result, String type) {
        if (type.equals(SUBJECT)) {
            Observable.from(result.subjects)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), Logger::e);
        } else if (type.equals(PROFESSOR)) {
            Observable.from(result.professors)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), Logger::e);
        }
    }
}
