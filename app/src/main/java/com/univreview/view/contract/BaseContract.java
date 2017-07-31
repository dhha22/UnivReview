package com.univreview.view.contract;

/**
 * Created by DavidHa on 2017. 7. 31..
 */

public interface BaseContract<T> {
    void attachView(T view);
    void detachView();
}
