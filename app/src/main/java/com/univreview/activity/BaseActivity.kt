package com.univreview.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.univreview.R
import com.univreview.listener.OnBackPressedListener
import com.univreview.util.Util

/**
 * Created by DavidHa on 2017. 8. 4..
 */
open class BaseActivity : AppCompatActivity() {
    private var onBackPressedListener: OnBackPressedListener? = null
    protected lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        progressDialog = Util.progressDialog(this)
    }

    protected fun setFullScreen() {
        setTheme(R.style.AppTheme_NoActionBar_FullScreen)
    }

    protected fun setTranslucent() {
        setTheme(R.style.AppTheme_Translucent)
    }

    fun setOnBackPressedListener(onBackPressedListener: OnBackPressedListener) {
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
        super.onDestroy()
        progressDialog.dismiss()
    }
}
