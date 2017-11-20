package com.univreview.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.WindowManager
import com.univreview.R
import com.univreview.fragment.ProfileEditFragment
import com.univreview.fragment.SearchFragment
import com.univreview.fragment.TestFragment
import com.univreview.fragment.login.RegisterEmailFragment
import com.univreview.fragment.login.RegisterUserInfoFragment
import com.univreview.fragment.review.ReviewDetailFragment
import com.univreview.log.Logger

/**
 * Created by DavidHa on 2017. 11. 2..
 */
class NavigationActivity : BaseActivity() {
    companion object {
        var fragment: Fragment? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.v("navigation activity onCreate")
        if (fragment != null) {
            when (fragment) {
                is RegisterEmailFragment,
                is RegisterUserInfoFragment,
                is ReviewDetailFragment,
                is ProfileEditFragment -> window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                else -> window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            }
            setContentView(R.layout.activity_navigation)
            supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()
            fragment = null
        }
    }

}