package com.univreview.fragment

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.BusProvider
import com.univreview.model.Review
import com.univreview.util.Util
import com.univreview.view.Toolbar
import rx.subjects.PublishSubject

/**
 * Created by DavidHa on 2017. 8. 4..
 */
open class BaseFragment : Fragment() {
    companion object {
        private val HOME_FRAGMENT = "com.univreview.fragment.HomeFragment"
        @JvmField
        var reviewPublishSubject: PublishSubject<Review> = PublishSubject.create()
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

    fun showSimpleMsgDialog(msg: String) {
        Util.simpleMessageDialog(context, msg)
    }

    fun showTicketDialog() {
        AlertDialog.Builder(context)
                .setMessage("리뷰티켓을 구매해주시길 바랍니다.")
                .setPositiveButton("구매하기", { _, _ ->
                    activity.finish()
                    Navigator.goPointList(context, App.point)
                })
                .setNegativeButton("취소", { _, _ -> activity.finish() })
                .setCancelable(false)
                .show()
    }
}