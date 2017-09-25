package com.univreview.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.univreview.fragment.HomeFragment
import com.univreview.fragment.MyPageFragment
import java.util.*

/**
 * Created by DavidHa on 2017. 9. 25..
 */
class MainAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val fragments = Arrays.asList(HomeFragment.getInstance(), MyPageFragment.getInstance());

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }
}