package com.univreview.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.univreview.R
import com.univreview.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_user_auth_completed.*

/**
 * Created by DavidHa on 2017. 11. 2..
 */
class UserAuthCompletedFragment : BaseFragment() {
    companion object {
        fun getInstance(): UserAuthCompletedFragment {
            return UserAuthCompletedFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_user_auth_completed, container, false)
        ButterKnife.bind(this, view)
        toolbar.visibility = View.GONE
        rootLayout.addView(view)
        completeBtn.setOnClickListener { activity.finish() }
        return rootLayout
    }
}