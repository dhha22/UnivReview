package com.univreview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.univreview.R;

/**
 * Created by DavidHa on 2017. 4. 1..
 */
public class BaseWriteActivity extends BaseActivity {
    boolean cancelOut = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if(cancelOut){
            super.onBackPressed();
        }else {
            new AlertDialog.Builder(this, R.style.customDialog)
                    .setMessage("작성을 취소하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        cancelOut = true;
                        onBackPressed();
                    })
                    .setNegativeButton("아니오", null)
                    .setCancelable(false)
                    .show();
        }
    }
}
