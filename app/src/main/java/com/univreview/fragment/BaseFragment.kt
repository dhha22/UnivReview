package com.univreview.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.univreview.Navigator
import com.univreview.R
import com.univreview.activity.BaseActivity
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.util.Util
import com.univreview.view.Toolbar

/**
 * Created by DavidHa on 2017. 8. 4..
 */
open class BaseFragment : Fragment() {
    companion object {
        private val HOME_FRAGMENT = "com.univreview.fragment.HomeFragment"
    }

    protected lateinit var rootLayout: LinearLayout
    protected lateinit var toolbar: Toolbar
    protected lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context as BaseActivity).activityResultSubject.subscribe { onActivityResult(it) }
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(getString(R.string.progress_dialog_msg))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.v("on create view: " + this.javaClass.name)
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
        if (javaClass.name != HOME_FRAGMENT) {
            Logger.v("on resume: " + this.javaClass.name)
        }
    }

    override fun onPause() {
        super.onPause()
        if (this.javaClass.name != HOME_FRAGMENT) {
            Logger.v("on pause: " + this.javaClass.name)
        }
    }

    override fun onDestroy() {
        progressDialog.dismiss()
        Logger.v("on destroy: " + this.javaClass.name)
        super.onDestroy()
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

    fun showWriteStorageAuthDialog() {
        AlertDialog.Builder(context)
                .setTitle("도움말")
                .setMessage("사진을 올리려면 저장공간 권한이 필요합니다." + "\n[설정]->[권한]에서 해당권한(저장공간)을 활성화해주세요.")
                .setNegativeButton("거부") { _, _ -> (context as Activity).finish() }
                .setPositiveButton("설정") { _, _ -> Navigator.goAppSetting(context) }
                .setCancelable(false)
                .show()
    }

    fun showCameraAuthDialog() {
        AlertDialog.Builder(context)
                .setTitle("도움말")
                .setMessage("사진을 올리려면 카메라 권한이 필요합니다." + "\n[설정]->[권한]에서 해당권한(카메라)을 활성화해주세요.")
                .setNegativeButton("거부") { _, _ -> (context as Activity).finish() }
                .setPositiveButton("설정") { _, _ -> Navigator.goAppSetting(context) }
                .setCancelable(false)
                .show()
    }

    fun showTicketDialog() {
        AlertDialog.Builder(context)
                .setMessage("리뷰티켓을 구매해주시길 바랍니다.")
                .setPositiveButton("구매하기", { _, _ ->
                    activity.finish()
                    Navigator.goPointList(context)
                })
                .setNegativeButton("취소", { _, _ -> activity.finish() })
                .setCancelable(false)
                .show()
    }

    open protected fun onActivityResult(activityResultEvent: ActivityResultEvent) {

    }
}