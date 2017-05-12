package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public class SearchListItemView extends FrameLayout {
    @BindView(R.id.name_txt) TextView nameTxt;
    private Context context;

    public SearchListItemView(Context context) {
        this(context, null);
    }

    public SearchListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.search_list_item, this, true);
        ButterKnife.bind(this);
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    public void setTextSize(int size) {
        nameTxt.setTextSize(size);
    }

    public void setText(String str){
        nameTxt.setText(str);
    }

    public void setTextCenter(boolean isCenter){
        if(isCenter) {
            nameTxt.setGravity(Gravity.CENTER);
        }
    }

    public void setLeftPadding(int size) {
        nameTxt.setPadding((int) Util.dpToPx(context, size), App.dp12, App.dp12, App.dp12);
    }
}
