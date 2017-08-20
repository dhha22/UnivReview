package com.univreview.adapter.contract;

import com.univreview.listener.OnItemClickListener;
import com.univreview.model.model_kotlin.AbstractDataProvider;

/**
 * Created by DavidHa on 2017. 7. 14..
 */

public interface ReviewListAdapterContract {
    interface View{
        void setOnItemClickListener(OnItemClickListener itemClickListener);
    }

    interface Model{
        void addItem(AbstractDataProvider item);
        void clearItem();
        AbstractDataProvider getItem(int position);
    }
}
