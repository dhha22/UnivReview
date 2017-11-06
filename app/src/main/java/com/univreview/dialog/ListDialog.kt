package com.univreview.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.dhha22.bindadapter.BindAdapter
import com.dhha22.bindadapter.Item
import com.dhha22.bindadapter.ItemView
import com.dhha22.bindadapter.listener.OnItemClickListener
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.DialogItem
import com.univreview.util.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.dialog_list.*
import kotlinx.android.synthetic.main.simple_list_item.view.*

/**
 * Created by DavidHa on 2017. 11. 6..
 */
class ListDialog(context: Context, data: List<String>, itemClickListener: OnItemClickListener) : Dialog(context) {
    private var adapter: BindAdapter

    init {
        adapter = BindAdapter(context).addLayout(DialogItemView::class.java)

        data.forEach {
            adapter.addItem(DialogItem(it))
        }
        adapter.notifyData()
        adapter.setOnItemClickListener(itemClickListener)
        Logger.v("LisDialog Constructor")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //외부 dim 처리
        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window!!.attributes = lpWindow
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.dialog_list)

        dialogRecyclerView.adapter = adapter
        dialogRecyclerView.addItemDecoration(SimpleDividerItemDecoration(context))
        cancelBtn.setOnClickListener { dismiss() }
    }

    class DialogItemView(context: Context) : ItemView(context) {
        init {
            setContentView(R.layout.simple_list_item)
            setFullSpan()
        }

        override fun setData(data: Item?) {
            super.setData(data)
            if (data is DialogItem) {
                text.text = data.name
            }
        }
    }
}