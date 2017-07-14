package com.univreview.adapter.contract;

import com.univreview.listener.OnItemLongClickListener;
import com.univreview.model.AbstractDataProvider;

/**
 * Created by DavidHa on 2017. 7. 13..
 */

public interface ReviewDetailAdapterContract {
    interface View{
        void setOnItemLongClickListener(OnItemLongClickListener clickListener);
    }

    interface  Model{
        void addItem(AbstractDataProvider item);
        void clearItem();
        AbstractDataProvider getItem(int position);
        void removeItem(int position);
    }
}
