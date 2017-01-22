package com.univreview.adapter;

import android.support.v7.widget.RecyclerView;

import com.univreview.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public abstract class CustomAdapter<T> extends RecyclerView.Adapter {
    protected List<T> list = new ArrayList<>();
    protected OnItemClickListener itemClickListener;
    public abstract T getItem(int position);

    public abstract void addItem(T item);

    public void setItem(int position, T item) {
    }

    public void clear() {
        if (list.size() > 0) {
            this.list.clear();
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

}
