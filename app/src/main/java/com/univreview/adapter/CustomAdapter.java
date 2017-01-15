package com.univreview.adapter;

import android.support.v7.widget.RecyclerView;

import com.univreview.listener.OnItemClickListener;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public abstract class CustomAdapter<T> extends RecyclerView.Adapter {
    public abstract T getItem(int position);

    public abstract void addItem(T item);

    public void setItem(int position, T item) {
    }

    public void setOnItemClickListener(OnItemClickListener listener){
    }

}
