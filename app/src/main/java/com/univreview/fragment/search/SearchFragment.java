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

import com.univreview.App;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.AbsListFragment;
import com.univreview.fragment.BaseFragment;
import com.univreview.listener.EndlessRecyclerViewScrollListener;
import com.univreview.listener.OnItemClickListener;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;
import com.univreview.model.Department;
import com.univreview.model.DepartmentModel;
import com.univreview.model.Major;
import com.univreview.model.MajorModel;
import com.univreview.model.SearchModel;
import com.univreview.network.Retro;
import com.univreview.util.RevealAnimationSetting;
import com.univreview.view.SearchListItemView;
import com.univreview.view.UnivReviewRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 16..
 */
public class SearchFragment extends AbsListFragment {
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.recycler_view)
    UnivReviewRecyclerView recyclerView;
    private String type;
    private SearchAdapter adapter;
    private long id;


    public static SearchFragment newInstance(String type, long id) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SearchFragment newInstance(String type, int id, String name) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        id = getArguments().getLong("id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        input.setHint(getHintStr(type));

        //recycler view
        adapter = new SearchAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                if (getLastVisibleItemPosition() == getTotalItemCount() - 1) {
                    lastItemExposed();
                }
            }
        });
        input.addTextChangedListener(textWatcher);
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Logger.v("search str: " + s.toString());
            page = DEFAULT_PAGE;
            callSearchApi(id, type, s.toString(), page);
        }
    };


    private void callSearchApi(long id, String type, String name, int page) {
        if (page == DEFAULT_PAGE) adapter.clear();
        switch (type) {
            case "university":
                callGetUniversityApi(name, page);
                break;
            case "department":
                callGetDepartmentApi(id, name, page);
                break;
            case "major":
                callGetMajorApi(id, name, page);
                break;
            case "subject":
                callGetSubjectApi(id,name, page);
                break;
            case "professor":
                callGetProfessorApi(id, name, page);
                break;
            default:
        }
    }

    private String getHintStr(String type) {
        switch (type) {
            case "university":
                return "대학교를 입력해주세요";
            case "department":
                return "학과군을 입력해주세요";
            case "major":
                return "학과를 입력해주세요";
            case "subject":
                return "과목을 입력해주세요";
            case "professor":
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
        callSearchApi(id, type, input.getText().toString(), DEFAULT_PAGE);
    }

    @Override
    public void loadMore() {
        Logger.v("load more");
        Logger.v("page: " + page);
        setStatus(Status.LOADING_MORE);
        callSearchApi(id, type, input.getText().toString(), page);
    }

    private class SearchAdapter extends CustomAdapter {
        private Context context;


        public SearchAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new SearchListItemView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).v.setData(list.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public AbstractDataProvider getItem(int position) {
            return list.get(position);
        }

        @Override
        public void addItem(AbstractDataProvider item) {
            list.add(item);
            notifyDataSetChanged();
        }


        protected class ViewHolder extends RecyclerView.ViewHolder {
            final SearchListItemView v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = (SearchListItemView) itemView;
                v.setOnClickListener(view -> itemClickListener.onItemClick(v, getAdapterPosition()));
            }
        }
    }

    //api
    private void callGetUniversityApi(String name, int page) {
        Retro.instance.searchService().getUniversities(name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result, "university"), error -> Logger.e(error));
    }

    private void callGetDepartmentApi(long id, String name, int page) {
        Retro.instance.searchService().getDepartments(id, name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result, "department"), error -> Logger.e(error));
    }

    private void callGetMajorApi(long id, String name, int page) {
        Retro.instance.searchService().getMajors(id, name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result, "major"), error -> Logger.e(error));
    }

    private void callGetProfessorApi(Long departmentId, String name, int page) {
        Retro.instance.searchService().getProfessors(App.UNIVERSITY_ID, departmentId, name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result, "professor"), error -> Logger.e(error));
    }

    private void callGetSubjectApi(Long majorId, String name, int page) {
        Retro.instance.searchService().getSubjects(App.UNIVERSITY_ID, majorId, name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(result, "subject"), error -> Logger.e(error));
    }

    public void response(SearchModel result, String type) {
        setResult(page);
        setStatus(Status.IDLE);
        if (type.equals("university")) {
            Observable.from(result.universities)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), error -> Logger.e(error));
        } else if (type.equals("department")) {
            Observable.from(result.departments)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), error -> Logger.e(error));
        } else if (type.equals("major")) {
            Observable.from(result.majors)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), error -> Logger.e(error));
        } else if (type.equals("professor")) {
            Observable.from(result.professors)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), error -> Logger.e(error));
        } else if (type.equals("subject")) {
            Observable.from(result.subjects)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> adapter.addItem(data), error -> Logger.e(error));
        }
    }


}
