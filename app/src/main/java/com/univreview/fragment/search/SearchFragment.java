package com.univreview.fragment.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.fragment.BaseFragment;
import com.univreview.model.Department;
import com.univreview.model.Major;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 16..
 */
public class SearchFragment extends BaseFragment {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private String type;
    private SearchAdapter adapter;
    private Context context;

    public static SearchFragment newInstance(String type){
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        this.context = getContext();
        ButterKnife.bind(this, view);
        adapter = makeAdapter(type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    private SearchAdapter makeAdapter(String type){
        switch (type){
            case "department":
                return new SearchAdapter<Department>();
            case "major":
                return new SearchAdapter<Major>();
            default:
                return null;
        }
    }



    private class SearchAdapter<T> extends CustomAdapter<T>{
        private List<T> list = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
