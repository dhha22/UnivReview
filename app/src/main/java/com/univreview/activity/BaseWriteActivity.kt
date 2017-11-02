package com.univreview.activity

import android.support.v7.app.AlertDialog
import com.univreview.R

/**
 * Created by DavidHa on 2017. 11. 2..
 */
class BaseWriteActivity : BaseActivity() {
    private var cancelOut:Boolean = false

    override fun onBackPressed() {
        if (cancelOut) {
            super.onBackPressed()
        } else {
            AlertDialog.Builder(this, R.style.customDialog)
                    .setMessage("작성을 취소하시겠습니까?")
                    .setPositiveButton("예") { _, _ ->
                        cancelOut = true
                        onBackPressed()
                    }
                    .setNegativeButton("아니오", null)
                    .setCancelable(false)
                    .show()
        }
    }
}