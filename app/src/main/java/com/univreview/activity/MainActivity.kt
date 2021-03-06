package com.univreview.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.adapter.MainAdapter
import com.univreview.log.Logger
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by DavidHa on 2017. 9. 25..
 */
class MainActivity : BaseActivity() {

    private val INDEX_SEARCH = 0
    private val INDEX_MYPAGE = 1
    private var requestCode: Int = 0
    private var resultCode: Int = 0
    private var data: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        val adapter = MainAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = adapter.count
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageSelected(position: Int) {
                bottomBar.selectTabAtPosition(getCurPagerPosition(position))
            }
        })

        // bottom bar 눌렀을 경우
        bottomBar.setOnTabSelectListener {
            when (it) {
                R.id.tab_search -> viewPager.setCurrentItem(INDEX_SEARCH, false)
                R.id.tab_upload -> {
                    Observable.timer(200, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { bottomBar.selectTabAtPosition(getCurPagerPosition()) }
                    //com.univreview.Navigator.goTest(this)
                    Navigator.goUploadReview(this)
                }
                R.id.tab_mypage -> viewPager.setCurrentItem(INDEX_MYPAGE, false)
            }
        }
    }

    fun getCurPagerPosition(position: Int = viewPager.currentItem): Int {
        return if (position == 0) {
            position
        } else {
            position + 1
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.requestCode = requestCode
        this.resultCode = resultCode
        this.data = data
        Logger.v("navigation activity on activity result")

    }

    override fun onResume() {
        super.onResume()
        App.setCurrentActivity(this)

    }


}