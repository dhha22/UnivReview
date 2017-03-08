package com.univreview.model;

import android.util.Pair;

import com.univreview.log.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 3. 7..
 */
public class MajorExpandableDataProvider extends AbstractExpandableDataProvider{
    private List<Pair<GroupData, List<ChildData>>> mData;

    // for undo group item
    private Pair<GroupData, List<ChildData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private ChildData mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    public MajorExpandableDataProvider() {
        final String groupItems = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String childItems = "abc";

        mData = new LinkedList<>();

        for (int i = 0; i < groupItems.length(); i++) {
            //noinspection UnnecessaryLocalVariable
            final long groupId = i;
            final String groupText = Character.toString(groupItems.charAt(i));
            final ConcreteGroupData group = new ConcreteGroupData(groupId, groupText);
            final List<ChildData> children = new ArrayList<>();

            for (int j = 0; j < childItems.length(); j++) {
                final long childId = group.generateNewChildId();
                final String childText = Character.toString(childItems.charAt(j));

                children.add(new ConcreteChildData(childId, childText));
            }

            mData.add(new Pair<GroupData, List<ChildData>>(group, children));
        }
    }

    public MajorExpandableDataProvider(List<Major> parentBrands) {


        mData = new LinkedList<>();
        for (int i = 0; i < parentBrands.size(); i++) {
            final long groupId = i;
            final String groupText = parentBrands.get(i).name;
            final ConcreteGroupData group = new ConcreteGroupData(groupId, groupText);
            final List<ChildData> children = new ArrayList<>();

            for (int j = 0; j < parentBrands.get(i).subject.size(); j++) {
                final long childId =  parentBrands.get(i).subject.get(j).getId();
                final String childText = parentBrands.get(i).subject.get(j).getName();
                Logger.v("child txt" + childText);
                children.add(new ConcreteChildData(childId, childText));
            }

            mData.add(new Pair<>(group, children));
        }

    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<ChildData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }


    public static final class ConcreteGroupData extends GroupData {

        private final long mId;
        private final String mText;
        private boolean mPinned;
        private long mNextChildId;

        ConcreteGroupData(long id, String text) {
            mId = id;
            mText = text;
            mNextChildId = 0;
        }

        @Override
        public long getGroupId() {
            return mId;
        }


        @Override
        public String getText() {
            return mText;
        }


        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public long generateNewChildId() {
            final long id = mNextChildId;
            mNextChildId += 1;
            return id;
        }
    }

    public static final class ConcreteChildData extends ChildData {

        private long mId;
        private final String mText;
        private boolean mPinned;

        ConcreteChildData(long id, String text) {
            mId = id;
            mText = text;
        }

        @Override
        public long getChildId() {
            return mId;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public void setChildId(long id) {
            this.mId = id;
        }

        @Override
        public String getChildName() {
            return mText;
        }
    }
}
