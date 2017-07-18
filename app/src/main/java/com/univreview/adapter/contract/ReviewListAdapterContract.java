package com.univreview.adapter.contract;

import com.univreview.model.AbstractDataProvider;

/**
 * Created by DavidHa on 2017. 7. 14..
 */

public interface ReviewListAdapterContract {
    interface View{

    }

    interface Model{
        void addItem(AbstractDataProvider item);
        void clearItem();
    }
}
