package com.univreview.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.listener.OnBackPressedListener
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.util.Util
import rx.subjects.PublishSubject

/**
 * Created by DavidHa on 2017. 8. 4..
 */
open class BaseActivity : AppCompatActivity() {
    private var onBackPressedListener: OnBackPressedListener? = null
    protected lateinit var progressDialog: ProgressDialog
    val activityResultSubject  =  PublishSubject.create<ActivityResultEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = Util.progressDialog(this)
    }


    protected fun setTranslucent() {
        setTheme(R.style.AppTheme_Translucent)
    }

    fun setOnBackPressedListener(onBackPressedListener: OnBackPressedListener?) {
        this.onBackPressedListener = onBackPressedListener
    }

    override fun onBackPressed() {
        onBackPressedListener?.doBack()
        onBackPressedListener ?: super.onBackPressed()
    }

    fun showProgress() {
        progressDialog.show()
    }

    fun dismissProgress() {
        progressDialog.dismiss()
    }

    override fun onDestroy() {
        progressDialog.dismiss()
        activityResultSubject.onCompleted()
        super.onDestroy()
    }

    fun showTicketDialog() {
        AlertDialog.Builder(this)
                .setMessage("리뷰티켓을 구매해주시길 바랍니다.")
                .setPositiveButton("구매하기", { _, _ ->
                    this.finish()
                    Navigator.goPointList(this)
                })
                .setNegativeButton("취소", { _, _ -> this.finish() })
                .setCancelable(false)
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.v("on activity result")
        activityResultSubject.onNext(ActivityResultEvent(requestCode, resultCode, data))

    }


}
