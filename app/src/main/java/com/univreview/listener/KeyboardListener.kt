package com.univreview.listener

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.univreview.log.Logger
import com.univreview.util.Util

/**
 * Created by DavidHa on 2017. 9. 24..
 */
class KeyboardListener(val context: Context, val editText: EditText) : View.OnKeyListener {
    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            Logger.v("enter")
            Util.hideKeyboard(context, editText)
            return true
        }
        return false
    }

}