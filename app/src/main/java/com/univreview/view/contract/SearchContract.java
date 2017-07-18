package com.univreview.view.contract;

/**
 * Created by DavidHa on 2017. 7. 15..
 */

public interface SearchContract {
    interface View{

    }

    void attachView(View view);
    void detachView();
}
