package com.univreview.adapter.contract;

import com.univreview.listener.OnItemLongClickListener;
import com.univreview.model.model_kotlin.AbstractDataProvider;

/**
 * Created by DavidHa on 2017. 7. 13..
 */

public interface ReviewDetailAdapterContract {
    interface View{
        void setOnItemLongClickListener(OnItemLongClickListener clickListener);
    }

    interface  Model{
        void addItem(AbstractDataProvider item);
        void addLastItem(AbstractDataProvider item);
        void clearItem();
        AbstractDataProvider getItem(int position);
        int getItemCount();
        void removeItem(int position);
    }
}
