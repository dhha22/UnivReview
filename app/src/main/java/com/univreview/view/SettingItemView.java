package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 2. 19..
 */
public class SettingItemView extends FrameLayout {
    @BindView(R.id.title_txt) TextView titleTxt;
    @BindView(R.id.preview_txt) TextView previewTxt;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.setting_item, this, true);
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ));
        ButterKnife.bind(this);

    }

    public void setTitle(String title){
        titleTxt.setText(title);
    }

    public void setPreviewTxt(String preview){
        previewTxt.setText(preview);
    }
}
