package com.univreview.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.univreview.R;
import com.univreview.listener.OnBackPressedListener;
import com.univreview.util.Util;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
/*public class BaseActivity extends AppCompatActivity {
    protected OnBackPressedListener onBackPressedListener;
    protected ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        progressDialog = Util.progressDialog(this);
    }

    protected void setFullScreen(){
        setTheme(R.style.AppTheme_NoActionBar_FullScreen);
    }

    protected void setTranslucent(){
        setTheme(R.style.AppTheme_Translucent);
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public void showProgress(){
        if(progressDialog != null){
            progressDialog.show();
        }
    }

    public void dismissProgress(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}*/
