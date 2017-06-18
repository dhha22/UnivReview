package com.univreview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.univreview.listener.OnItemClickListener;
import com.univreview.listener.OnItemLongClickListener;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public abstract class CustomAdapter extends RecyclerView.Adapter {
    protected Context context;

    public CustomAdapter(Context context) {
        this.context = context;
    }

    protected List<AbstractDataProvider> list = new ArrayList<>();
    protected OnItemClickListener itemClickListener;
    protected OnItemLongClickListener itemLongClickListener;

    public AbstractDataProvider getItem(int position) {
        if (list.size() > position) {
            return list.get(position);
        }
        return null;
    }

    public void addItem(AbstractDataProvider item){
        list.add(item);
        notifyDataSetChanged();
    }

    public void setItem(int position, AbstractDataProvider item) {
        list.set(position, item);
        notifyDataSetChanged();
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

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.itemLongClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
