package com.univreview.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.univreview.R;
import com.univreview.fragment.HomeFragment;
import com.univreview.fragment.MypageFragment;
import com.univreview.fragment.upload.UploadFragment;

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
                    replaceFragment(HomeFragment.newInstance());
                    break;
                case R.id.tab_upload:
                    replaceFragment(UploadFragment.newInstance());
                    break;
                case R.id.tab_mypage:
                    replaceFragment(MypageFragment.newInstance());
                    break;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment, fragment.getClass().getName()).commit();
    }
}
