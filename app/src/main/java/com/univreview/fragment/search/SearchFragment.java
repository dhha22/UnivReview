package com.univreview.fragment.search;

import android.content.Context;
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

import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.BaseFragment;
import com.univreview.log.Logger;
import com.univreview.model.Department;
import com.univreview.model.DepartmentModel;
import com.univreview.model.Major;
import com.univreview.model.MajorModel;
import com.univreview.network.Retro;
import com.univreview.util.RevealAnimationSetting;
import com.univreview.view.SearchListItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 16..
 */
public class SearchFragment extends BaseFragment {
    @BindView(R.id.input) EditText input;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private String type;
    private SearchAdapter adapter;
    private Context context;
    private int id;

    public static SearchFragment newInstance(String type, int id){
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        id = getArguments().getInt("id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        this.context = getContext();
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        input.setHint(getHintStr(type));

        //recycler view
        adapter = constructAdapter(type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        input.addTextChangedListener(textWatcher);
        callSearchApi(id, type, null);
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
            callSearchApi(id, type, s.toString());
        }
    };


    private void callSearchApi(int id, String type, String name) {
        switch (type) {
            case "department":
                callGetDepartmentApi(id, name);
                break;
            case "major":
                callGetMajorApi(id, name);
                break;
            default:
        }
    }

    private String getHintStr(String type){
        switch (type){
            case "department":
                return "학과군을 입력해주세요";
            case "major":
                return "학과를 입력해주세요";
            default:
                return "";
        }
    }

    private SearchAdapter constructAdapter(String type){
        switch (type){
            case "department":
                return new SearchAdapter<Department>(context);
            case "major":
                return new SearchAdapter<Major>(context);
            default:
                return null;
        }
    }



    private class SearchAdapter<T> extends CustomAdapter<T>{
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
            if(list.get(position) instanceof Department) {
                ((ViewHolder) holder).v.setData(((Department) (list.get(position))).name);
            }else if(list.get(position) instanceof Major){
                ((ViewHolder) holder).v.setData(((Major) (list.get(position))).name);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public T getItem(int position) {
            return list.get(position);
        }


        @Override
        public void addItem(T item) {
            list.add(item);
            notifyDataSetChanged();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            final SearchListItemView v;
            public ViewHolder(View itemView) {
                super(itemView);
                v = (SearchListItemView)itemView;
            }
        }
    }

    private void callGetDepartmentApi(int id, String name){
        Retro.instance.searchService().getDepartments(id, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(() -> adapter.clear())
                .subscribe(result -> responseDepartment(result), error -> Logger.e(error));
    }

    private void callGetMajorApi(int id, String name){
        Retro.instance.searchService().getMajors(id, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(() -> adapter.clear())
                .subscribe(result-> responseMajor(result), error -> Logger.e(error));
    }

    public void responseDepartment(DepartmentModel result){
        Observable.from(result.departments)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> adapter.addItem(data), error -> Logger.e(error));
    }

    public void responseMajor(MajorModel result){
        Observable.from(result.majors)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> adapter.addItem(data), error -> Logger.e(error));
    }





}
