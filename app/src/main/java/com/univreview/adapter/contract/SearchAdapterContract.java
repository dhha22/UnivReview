package com.univreview.adapter.contract;

import com.univreview.model.model_kotlin.AbstractDataProvider;

/**
 * Created by DavidHa on 2017. 7. 20..
 */

public interface SearchAdapterContract {
    interface View{

    }

    interface Model{
        void clear();
        void addItem(AbstractDataProvider item);
    }
}
