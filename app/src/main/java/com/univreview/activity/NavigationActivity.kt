package com.univreview.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.WindowManager
import com.squareup.otto.Produce
import com.univreview.R
import com.univreview.fragment.ProfileEditFragment
import com.univreview.fragment.SearchFragment
import com.univreview.fragment.TestFragment
import com.univreview.fragment.login.RegisterEmailFragment
import com.univreview.fragment.login.RegisterUserInfoFragment
import com.univreview.fragment.review.ReviewDetailFragment
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.model.BusProvider

/**
 * Created by DavidHa on 2017. 11. 2..
 */
class NavigationActivity : BaseActivity() {
    companion object {
        var fragment: Fragment? = null
    }

    private var requestCode: Int = 0
    private var resultCode: Int = 0
    private var data: Intent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.v("navigation activity onCreate")
        if (fragment != null) {
            if (fragment is SearchFragment) {
                Logger.v("set translucent")
                setTranslucent()
            } else if (fragment is RegisterEmailFragment ||
                    fragment is RegisterUserInfoFragment ||
                    fragment is ReviewDetailFragment ||
                    fragment is TestFragment ||
                    fragment is ProfileEditFragment) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            } else {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            }
            setContentView(R.layout.activity_navigation)
            supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commitNow()
            fragment = null
        }
    }


    override fun onResume() {
        super.onResume()
        Logger.v("register activity result")
        BusProvider.newInstance().register(this)
    }

    override fun onPause() {
        super.onPause()
        this.resultCode = 0
        this.requestCode = this.resultCode
        this.data = null
        BusProvider.newInstance().unregister(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            this.requestCode = requestCode
            this.resultCode = resultCode
            this.data = data
            Logger.v("navigation activity on activity result")
            BusProvider.newInstance().post(produceActivityResultEvent())
        }
    }


    @Produce
    fun produceActivityResultEvent(): ActivityResultEvent {
        return ActivityResultEvent(requestCode, resultCode, data)
    }


    override fun finish() {
        super.finish()
        Logger.v("finish Navigation Activity")

    }
}