package com.univreview.fragment.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.adapter.SearchAdapter;
import com.univreview.adapter.contract.SearchAdapterContract;
import com.univreview.fragment.AbsListFragment;
import com.univreview.listener.EndlessRecyclerViewScrollListener;
import com.univreview.log.Logger;
import com.univreview.model.enumeration.ReviewSearchType;
import com.univreview.util.Util;
import com.univreview.view.SearchListItemView;
import com.univreview.view.UnivReviewRecyclerView;
import com.univreview.view.contract.SearchContract;
import com.univreview.view.presenter.SearchPresenter;
import com.univreview.widget.PreCachingLayoutManager;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by DavidHa on 2017. 1. 16..
 */
public class SearchFragment extends AbsListFragment implements SearchContract.View {

    @BindView(R.id.input) EditText input;
    @BindView(R.id.delete_btn) ImageButton deleteBtn;
    @BindView(R.id.recycler_view) UnivReviewRecyclerView recyclerView;
    private ReviewSearchType type;
    private SearchAdapter adapter;
    private Long id;
    private boolean isReviewSearch;
    private Timer timer;
    private SearchPresenter presenter;

    public static SearchFragment newInstance(ReviewSearchType type, long id, boolean isReviewSearch) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putLong("id", id);
        bundle.putBoolean("isReviewSearch", isReviewSearch);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (ReviewSearchType) getArguments().getSerializable("type");
        id = getArguments().getLong("id");
        if(id == 0) id = null;
        isReviewSearch = getArguments().getBoolean("isReviewSearch");
        presenter = new SearchPresenter();
        presenter.view = this;

        if (isReviewSearch) {
            presenter.setSubjectType(null);
        } else {
            presenter.setSubjectType("M");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        toolbar.setBackBtnVisibility(true);
        init();
        rootLayout.addView(view);
        rootLayout.setBackgroundColor(Util.getColor(getContext(), R.color.searchBgColor));
        return rootLayout;
    }

    private void init() {
        Logger.v("type: " + type.getTypeName());
        Logger.v("id: " + id);
        input.setHint(getHintStr(type));
        deleteBtn.setOnClickListener(v -> input.setText(null));
        input.addTextChangedListener(textWatcher);
        setRecyclerView();
    }

    private void setRecyclerView(){
        //recycler view
        adapter = new SearchAdapter(getContext());
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        // pagination 추후 적용
        /*recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                if (getLastVisibleItemPosition() == getTotalItemCount() - 1) {
                    lastItemExposed();
                }
            }
        });*/
        presenter.setSearchAdapterModel(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            Logger.v("search position: " + position);
            if (isReviewSearch) {   // 일반적인 review search
                String name = adapter.getItem(position).getName();
                input.setText(name);
                input.setSelection(name.length());
                Navigator.goReviewList(getContext(), type, adapter.getItem(position).getId(), name);
            } else {    // 보여주는 search
                Intent intent = new Intent();
                Logger.v("id : " + adapter.getItem(position).getId());
                Logger.v("type : " + type.getTypeName());
                intent.putExtra("id", adapter.getItem(position).getId());
                intent.putExtra("name", adapter.getItem(position).getName());
                intent.putExtra("type", type);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().onBackPressed();
            }
        });
    }

    // search input textWatcher

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(timer != null){
                timer.cancel();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Logger.v("search str: " + s.toString());
            if(s.length()>0){
                deleteBtn.setVisibility(View.VISIBLE);
            }else{
                deleteBtn.setVisibility(View.INVISIBLE);
            }
            setPage(getDEFAULT_PAGE());
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(() -> callSearchApi(id, type, s.toString(), getPage()));
                }
            }, 300);
        }
    };


    private void callSearchApi(Long id, ReviewSearchType type, String name, int page) {
        switch (type) {
            case UNIVERSITY:
                presenter.searchUniversity(name, page);
                break;
            case DEPARTMENT:
                presenter.searchDepartment(id, name, page);
                break;
            case MAJOR:
                presenter.searchMajor(id, name, page);
                break;
            case SUBJECT:
                presenter.searchSubject(id, name, page);
                break;
            case PROFESSOR:
                presenter.searchProfessor(id, name, page);
                break;
            case PROF_FROM_SUBJ:
                presenter.searchProfFromSubj(id, name, page);
                break;
        }
    }

    private String getHintStr(ReviewSearchType type) {
        switch (type) {
            case UNIVERSITY:
                return "대학교를 입력해주세요";
            case DEPARTMENT:
                return "학과군을 입력해주세요";
            case MAJOR:
                return "학과를 입력해주세요";
            case SUBJECT:
                return "과목을 입력해주세요";
            case PROFESSOR:
                return "교수명을 입력해주세요";
            case PROF_FROM_SUBJ:
                return "교수명을 입력해주세요";
            default:
                return "";
        }
    }


    @Override
    public UnivReviewRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void refresh() {
        setStatus(Status.REFRESHING);
        callSearchApi(id, type, input.getText().toString(), getDEFAULT_PAGE());
    }

    @Override
    public void loadMore() {
       /* Logger.v("load more");
        Logger.v("page: " + getPage());
        setStatus(Status.LOADING_MORE);
        callSearchApi(id, type, input.getText().toString(), getPage());*/
    }



    @Override
    public void onPause() {
        super.onPause();
        Util.hideKeyboard(getContext(), input);
    }


    @Override
    public void onDestroy() {
        presenter.stopSearch();
        super.onDestroy();
    }
}
