package com.univreview.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.roughike.bottombar.BottomBar;
import com.squareup.otto.Produce;
import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.HomeFragment;
import com.univreview.fragment.MypageFragment;
import com.univreview.log.Logger;
import com.univreview.model.ActivityResultEvent;
import com.univreview.model.BusProvider;
import com.univreview.widget.NonSwipeableViewPager;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.bottom_bar) BottomBar bottomBar;
    @BindView(R.id.viewpager) NonSwipeableViewPager viewPager;
    private PagerAdapter adapter;
    private static final int INDEX_HOME = 0;
    private static final int INDEX_MYPAGE = 1;
    private int requestCode;
    private int resultCode;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }
    //test test test

    private void init() {
        adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logger.v("main page position: " + position);
                if (position == 0) {
                    bottomBar.selectTabAtPosition(position);
                } else {
                    bottomBar.selectTabAtPosition(position + 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_home:
                    viewPager.setCurrentItem(INDEX_HOME, false);
                    break;
                case R.id.tab_upload:
                    int position = viewPager.getCurrentItem() == 0 ? viewPager.getCurrentItem() : viewPager.getCurrentItem() + 1;
                    new Handler().postDelayed(() -> bottomBar.selectTabAtPosition(position), 200);
                    Navigator.goUploadReview(this);
                    break;
                case R.id.tab_mypage:
                    viewPager.setCurrentItem(INDEX_MYPAGE, false);
                    break;
            }
        });

        bottomBar.setOnTabReselectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_upload:
                    Navigator.goUploadReview(this);
                    break;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.setCurrentActivity(this);
        BusProvider.newInstance().register(this);
    }

    @Override protected void onPause() {
        super.onPause();
        BusProvider.newInstance().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.requestCode =requestCode;
        this.resultCode = resultCode;
        this.data =data;
        Logger.v("navigation activity on activity result");
        BusProvider.newInstance().post(produceActivityResultEvent());
    }


    @Produce
    public ActivityResultEvent produceActivityResultEvent() {
        return new ActivityResultEvent(requestCode, resultCode, data);
    }


    private class PagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentList = Arrays.asList(HomeFragment.newInstance(), MypageFragment.newInstance(App.userId));

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}
