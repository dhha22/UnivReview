package com.univreview.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 2. 1..
 */
public class Toolbar extends FrameLayout {
    @BindView(R.id.back_btn) Button backBtn;
    @BindView(R.id.title_txt) TextView titleTxt;
    @BindView(R.id.cancel_btn) Button cancelBtn;
    private Context context;

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.toolbar, this, true);
        ButterKnife.bind(this);
        this.context = context;
    }


    public void setBackBtnVisibility(boolean isVisible) {
        if (isVisible) {
            backBtn.setVisibility(VISIBLE);
            backBtn.setOnClickListener(v -> ((Activity) context).onBackPressed());
        } else {
            backBtn.setVisibility(GONE);
        }
    }

    public void setTitleTxt(String title){
        titleTxt.setText(title);
    }

    public void setCancelBtnVisibility(boolean isVisible) {
        if (isVisible) {
            cancelBtn.setVisibility(VISIBLE);
            cancelBtn.setOnClickListener(v -> ((Activity) context).onBackPressed());
        } else {
            cancelBtn.setVisibility(GONE);
        }
    }



}
