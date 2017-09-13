package com.univreview.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.R;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 7. 9..
 */

public class CommentInput extends FrameLayout {
    @BindView(R.id.input) EditText input;
    @BindView(R.id.send) TextView send;

    public CommentInput(@NonNull Context context) {
        this(context, null);
    }

    public CommentInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentInput(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.comment_input, this, true);
        ButterKnife.bind(this);
    }

    public String getInputMsg() {
        String inputStr = input.getText().toString();
        input.setText(null);
        Util.hideKeyboard(getContext(), input);
        return inputStr.length() > 0 ? inputStr : null;
    }

    public void setSendListener(OnClickListener listener){
        send.setOnClickListener(listener);
    }
}
