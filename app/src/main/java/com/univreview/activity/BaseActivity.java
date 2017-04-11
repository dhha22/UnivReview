package com.univreview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.univreview.R;
import com.univreview.listener.OnBackPressedListener;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class BaseActivity extends AppCompatActivity {
    protected OnBackPressedListener onBackPressedListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
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

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        } else {
            super.onBackPressed();
        }
    }
}
