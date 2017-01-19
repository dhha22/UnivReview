package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public class SearchListItemView extends FrameLayout {
    @BindView(R.id.name_txt) TextView nameTxt;
    public SearchListItemView(Context context) {
        this(context, null);
    }

    public SearchListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.search_list_item, this, true);
        ButterKnife.bind(this);

    }

    public void setData(String str){
        nameTxt.setText(str);
    }
}
