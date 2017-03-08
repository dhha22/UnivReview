package com.univreview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.univreview.listener.OnExpandableItemClickListener;
import com.univreview.log.Logger;
import com.univreview.model.AbstractExpandableDataProvider;
import com.univreview.view.SearchListItemView;


/**
 * Created by DavidHa on 2017. 1. 11..
 */
public class ExpandableAdapter extends AbstractExpandableItemAdapter<ExpandableAdapter.SearchItemGroup, ExpandableAdapter.SearchItemChild> {
    private Context context;
    private AbstractExpandableDataProvider provider;
    private OnExpandableItemClickListener itemClickListener;

    public ExpandableAdapter(Context context, AbstractExpandableDataProvider provider) {
        this.context = context;
        this.provider = provider;
        setHasStableIds(true);
    }

    @Override
    public SearchItemGroup onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new SearchItemGroup(new SearchListItemView(context));
    }

    @Override
    public SearchItemChild onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new SearchItemChild(new SearchListItemView(context));
    }

    @Override
    public void onBindGroupViewHolder(SearchItemGroup holder, int groupPosition, int viewType) {
        final AbstractExpandableDataProvider.BaseData item = provider.getGroupItem(groupPosition);
        holder.v.setText(item.getText());
        holder.itemView.setClickable(true);
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            boolean isExpanded;
            if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_EXPANDED) != 0) {
                isExpanded = true;
            } else {
                isExpanded = false;
            }
            //holder.v.setExpandState(isExpanded);
        }

    }

    @Override
    public void onBindChildViewHolder(SearchItemChild holder, int groupPosition, int childPosition, int viewType) {
        // group item
        final AbstractExpandableDataProvider.ChildData item = provider.getChildItem(groupPosition, childPosition);
        Logger.v("child txt: " + item.getText());
        // set text
        holder.v.setText(item.getText());
        holder.v.setOnClickListener(v -> itemClickListener.onItemClick(v, groupPosition, childPosition));
    }

    @Override
    public int getGroupCount() {
        return provider.getGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return provider.getChildCount(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return provider.getGroupItem(groupPosition).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return provider.getChildItem(groupPosition, childPosition).getChildId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }


    public void setItemClickListener(OnExpandableItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public long getChildItemId(int groupPosition, int childPosition){
        return this.provider.getChildItem(groupPosition, childPosition).getChildId();
    }

    public String getChildItemName(int groupPosition, int childPosition){
        return this.provider.getChildItem(groupPosition, childPosition).getChildName();
    }


    @Override
    public boolean onCheckCanExpandOrCollapseGroup(SearchItemGroup holder, int groupPosition, int x, int y, boolean expand) {
        Logger.v("expand");
        if (provider.getGroupItem(groupPosition).isPinned()) {
            return false;
        }

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }
        return true;
    }

    protected class SearchItemGroup extends AbstractExpandableItemViewHolder {
        final SearchListItemView v;
        public SearchItemGroup(View itemView) {
            super(itemView);
            v = (SearchListItemView)itemView;
        }
    }

    protected class SearchItemChild extends AbstractExpandableItemViewHolder {
        final SearchListItemView v;

        public SearchItemChild(View itemView) {
            super(itemView);
            v = (SearchListItemView) itemView;
            v.setLeftPadding(16);
        }
    }
}
