package com.univreview.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.BusProvider
import com.univreview.util.Util
import com.univreview.view.Toolbar

/**
 * Created by DavidHa on 2017. 8. 4..
 */
open class BaseFragment : Fragment() {
    private companion object {
        val HOME_FRAGMENT = "com.univreview.fragment.HomeFragment"
    }

    protected lateinit var rootLayout: LinearLayout
    protected lateinit var toolbar: Toolbar
    protected lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.v("on create view: " + this.javaClass.name)
        progressDialog = ProgressDialog(context)

        rootLayout = LinearLayout(context)
        rootLayout.orientation = LinearLayout.VERTICAL
        toolbar = Toolbar(context)
        toolbar.setBackgroundColor(Util.getColor(context, R.color.colorPrimary))
        rootLayout.addView(toolbar)
        return rootLayout
    }

    protected fun setToolbarTransparent() {
        toolbar.setBackgroundColor(Util.getColor(context, R.color.transparent))
    }

    override fun onResume() {
        super.onResume()
        if (!javaClass.name.equals(HOME_FRAGMENT)) {
            Logger.v("on resume: " + this.javaClass.name)
            BusProvider.newInstance().register(this)
        }
    }

    override fun onPause() {
        super.onPause()
        if (this.javaClass.name != HOME_FRAGMENT) {
            Logger.v("on pause: " + this.javaClass.name)
            BusProvider.newInstance().unregister(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
        Logger.v("on destroy: " + this.javaClass.name)
    }

    fun showProgress() {
        progressDialog.show()
    }

    fun dismissProgress() {
        progressDialog.dismiss()
    }
}