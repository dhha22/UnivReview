package com.univreview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.univreview.App;
import com.univreview.listener.OnItemClickListener;
import com.univreview.listener.OnItemLongClickListener;
import com.univreview.log.Logger;
import com.univreview.model.AbstractDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 1. 13..
 */
public abstract class CustomAdapter extends RecyclerView.Adapter {
    protected Context context;
    protected View headerView;
    protected View footerView;
    protected final int HEADER = 1;
    protected final int FOOTER= 246;
    protected final int CONTENT = 100;
    private boolean hasFooterView = false;

    public CustomAdapter(Context context) {
        this.context = context;
    }

    public CustomAdapter(Context context, View headerView) {
        this.context = context;
        this.headerView = headerView;
    }

    public CustomAdapter(Context context, View headerView, View footerView) {
        this.context = context;
        this.headerView = headerView;
        this.footerView = footerView;
        hasFooterView = true;
    }

    protected List<AbstractDataProvider> list = new ArrayList<>();
    protected OnItemClickListener itemClickListener;
    protected OnItemLongClickListener itemLongClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerView != null && viewType == HEADER) {
            Logger.v("create header view");
            return new HeaderViewHolder(headerView);
        } else if (hasFooterView && viewType == FOOTER) {
            if (footerView == null) {
                Logger.v("create footer view");
                footerView = new View(context);
                footerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.dp56));
            }
            return new FooterViewHolder(footerView);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0) {
            return HEADER;
        } else if (list.size() > 0 && position < getCount()) {
            return CONTENT;
        }
        return FOOTER;
    }

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


    private int getCount() {
        if (headerView != null) {
            return list.size() + HEADER;
        }
        return list.size();
    }

    @Override
    public int getItemCount() {
        if (hasFooterView) {
            Logger.v("item count: " + (getCount() + 1));
            return getCount() + 1;
        }
        return getCount();
    }

    protected class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = itemView;
        }
    }

    protected class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View itemView) {
            super(itemView);
            footerView = itemView;
        }
    }
}
