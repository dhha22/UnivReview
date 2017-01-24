package com.univreview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.univreview.model.AbstractDataProvider;
import com.univreview.model.Review;
import com.univreview.view.LatestReviewItemView;

/**
 * Created by DavidHa on 2017. 1. 23..
 */
public class LatestReviewAdapter extends CustomAdapter{
    private Context context;

    public LatestReviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new LatestReviewItemView(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public Review getItem(int position) {
        return (Review)list.get(position);
    }

    @Override
    public void addItem(AbstractDataProvider item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        final LatestReviewItemView v;
        public ViewHolder(View itemView) {
            super(itemView);
            v = (LatestReviewItemView)itemView;
        }
    }
}