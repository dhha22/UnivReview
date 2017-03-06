package com.univreview.model;

/**
 * Created by DavidHa on 2017. 1. 11..
 */
public abstract class AbstractExpandableDataProvider {
    public static abstract class BaseData {

        public abstract String getText();

        public abstract void setPinned(boolean pinned);

        public abstract boolean isPinned();
    }

    public static abstract class GroupData extends BaseData {
        public abstract long getGroupId();
    }

    public static abstract class ChildData extends BaseData {
        public abstract long getChildId();
        public abstract String getChildName();
    }

    public abstract int getGroupCount();
    public abstract int getChildCount(int groupPosition);

    public abstract GroupData getGroupItem(int groupPosition);
    public abstract ChildData getChildItem(int groupPosition, int childPosition);

}
