package com.univreview.fragment

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.activity.BaseActivity
import com.univreview.listener.OnBackPressedListener

/**
 * Created by DavidHa on 2017. 9. 2..
 */
open class BaseWriteFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (activity as BaseActivity).setOnBackPressedListener(onBackPressedListener)
        return rootLayout
    }

    private val onBackPressedListener = OnBackPressedListener {
        AlertDialog.Builder(context)
                .setMessage("작성을 취소하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    (activity as BaseActivity).setOnBackPressedListener(null)
                    activity.onBackPressed()
                }
                .setNegativeButton("아니오", null)
                .setCancelable(false)
                .show()
    }
}