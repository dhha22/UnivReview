package com.univreview.fragment.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.univreview.App;
import com.univreview.R;
import com.univreview.activity.NavigationActivity;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.Professor;
import com.univreview.model.SearchModel;
import com.univreview.model.Subject;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
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
    private static final String SEARCH_PROFESSOR = "searchProfessor";
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

        rootLayout.setBackgroundColor(Util.getColor(context, R.color.searchBgColor));
        return rootLayout;
    }

    private void init(){
        adapter = new SearchAdapter(context);
        LinearLayout subRootLayout = new LinearLayout(context);
        subRootLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        subRootLayout.setGravity(Gravity.CENTER);
        recyclerView = new UnivReviewRecyclerView(context);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setMode(UnivReviewRecyclerView.Mode.DISABLED);
        recyclerView.setAdapter(adapter);
        if(type.equals(PROFESSOR)){
            callGetSubjectProfessor(id);
        }else if(type.equals(SUBJECT) || type.equals(SEARCH_PROFESSOR)){
            callGetProfessorSubject(id);
        }

        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent();
            Logger.v("id : " + adapter.getItem(position).getId());
            Logger.v("type : " + type);
            intent.putExtra("id", adapter.getItem(position).getId());
            intent.putExtra("name", adapter.getItem(position).getName());
            intent.putExtra("type", type);
            if (type.equals(SEARCH_PROFESSOR)) {
                intent.putExtra("detailId", ((Professor) adapter.getItem(position)).subjectDetailId);
            }
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().onBackPressed();
        });
        subRootLayout.addView(recyclerView);
        rootLayout.addView(subRootLayout);
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
                v.setTextSize(32);
                v.setTextCenter(true);
                v.setOnClickListener(v -> itemClickListener.onItemClick(v, getAdapterPosition()));
            }
        }

    }

    private void callGetProfessorSubject(long id){
        Retro.instance.searchService().getProfessorSubject(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::response,  ErrorUtils::parseError);
    }

    private void callGetSubjectProfessor(long id){
        Retro.instance.searchService().getSubjectProfessor(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::response,  ErrorUtils::parseError);
    }

    private void response(SearchModel result) {
        if (type.equals(SUBJECT)) {
            Professor professor = new Professor();
            professor.id = 0l;
            professor.name = "전체";
            result.professors.add(0, professor);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, result.professors.size() * (int) Util.dpToPx(context, 82)));
            Observable.from(result.professors)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), Logger::e);
        } else if (type.equals(PROFESSOR)) {
            Subject subject = new Subject();
            subject.id = 0l;
            subject.name = "전체";
            result.subjects.add(0, subject);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, result.subjects.size() * (int) Util.dpToPx(context, 82)));
            Observable.from(result.subjects)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), Logger::e);
        } else if(type.equals(SEARCH_PROFESSOR)){
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, result.professors.size() * (int) Util.dpToPx(context, 82)));
            Observable.from(result.professors)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), Logger::e);
        }
    }
}
