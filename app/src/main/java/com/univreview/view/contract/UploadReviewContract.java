package com.univreview.view.contract;

import java.util.List;

/**
 * Created by DavidHa on 2017. 7. 20..
 */

public interface UploadReviewContract {
    interface View{
        void setDialog(List<String> list);
    }
    void attachView(View view);
    void detachView();
}
