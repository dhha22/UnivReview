package com.univreview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.univreview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bottom_bar) BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId){
                case R.id.tab_home:
                    break;
                case R.id.tab_upload:
                    break;
                case R.id.tab_mypage:
                    break;
            }
        });
    }
}
