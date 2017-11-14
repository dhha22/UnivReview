package com.univreview.view

import android.content.Context
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView
import com.univreview.R
import com.univreview.model.Setting
import kotlinx.android.synthetic.main.setting_item.view.*

/**
 * Created by DavidHa on 2017. 11. 14..
 */
class SettingItemView(context: Context) : ItemView(context) {

    init {
        setContentView(R.layout.setting_item)
        setFullSpan()
    }

    override fun setData(data: Item) {
        super.setData(data)
        titleTxt.text = (data as Setting).name
        previewTxt.text = data.previewStr
    }


}