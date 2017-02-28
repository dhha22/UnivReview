package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.univreview.R;
import com.univreview.log.Logger;
import com.univreview.model.PointHistory;
import com.univreview.util.TimeUtil;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 2. 17..
 */
public class PointItemView extends FrameLayout {
    @BindView(R.id.time_txt) TextView timeTxt;
    @BindView(R.id.message_txt) TextView messageTxt;
    @BindView(R.id.point_txt) TextView pointTxt;
    private Context context;

    public PointItemView(Context context) {
        this(context, null);
    }

    public PointItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.point_item, this, true);
        ButterKnife.bind(this);
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.context = context;
    }

    public void setData(PointHistory pointHistory) {
        Logger.v("point history: " + pointHistory);
        if (pointHistory != null) {
            if (pointHistory.getPointType()) {
                pointTxt.setText("+" + pointHistory.point + "p");
                pointTxt.setTextColor(Util.getColor(context, R.color.colorPrimary));
            } else {
                pointTxt.setTextColor(Util.getColor(context, R.color.colorAccent));
                pointTxt.setText("-" + pointHistory.point + "p");
            }
            messageTxt.setText(pointHistory.message);
            timeTxt.setText(new TimeUtil().getPointFormat(pointHistory.historyDate));
        }
    }
}
