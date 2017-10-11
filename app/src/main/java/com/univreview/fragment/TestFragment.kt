package com.univreview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.R

/**
 * Created by DavidHa on 2017. 9. 28..
 */
class TestFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun getInstance():TestFragment{
            val fragment = TestFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}