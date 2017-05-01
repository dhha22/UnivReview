package com.univreview.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.activity.BaseActivity;
import com.univreview.fragment.review.ReviewDetailFragment;
import com.univreview.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 4. 23..
 */
public class RecommendRvDialog extends Dialog {
    @BindView(R.id.next) TextView next;
    @BindView(R.id.go_review_detail) TextView goReviewDetail;
    private Review review;
    private Context context;

    public RecommendRvDialog(Context context, Review review) {
        super(context);
        this.context = context;
        this.review = review;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //외부 dim 처리
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_recommend_rv);
        ButterKnife.bind(this);
        setCancelable(false);

        goReviewDetail.setOnClickListener(view -> {
            Navigator.goUploadReviewDetail(context, this.review);
            ((Activity)context).finish();
        });

        next.setOnClickListener(view -> {
            ReviewDetailFragment.isRefresh = true;
            Navigator.goReviewDetail(context, review);
            ((BaseActivity) context).setOnBackPressedListener(null);
            ((Activity)context).onBackPressed();
        });
    }
}
