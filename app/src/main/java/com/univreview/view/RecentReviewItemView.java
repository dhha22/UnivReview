package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.model.RandomImageModel;
import com.univreview.model.RecentReview;
import com.univreview.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 23..
 */
public class RecentReviewItemView extends FrameLayout {
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;
    @BindView(R.id.review_txt) TextView reviewTxt;
    @BindView(R.id.image) ImageView image;
    private RandomImageModel randomImageModel;
    private Context context;

    public RecentReviewItemView(Context context) {
        this(context, null);
    }

    public RecentReviewItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecentReviewItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.recent_review_item, this, true);
        int width = (int) (App.SCREEN_WIDTH / 2.2);
        int height = (int) (width * 1.25);
        setLayoutParams(new FrameLayout.LayoutParams(width, height));
        this.context = context;
        ButterKnife.bind(this);
        randomImageModel = new RandomImageModel();
        App.picasso.load(randomImageModel.getImageURL())
                .resize(width, height)
                .centerCrop()
                .into(image);

    }

    public void setData(RecentReview review) {
        if (review != null) {
            subjectTxt.setText(review.subjectName);
            professorTxt.setText(review.professorName);
            reviewTxt.setText(review.reviewDetail);
            Review data = new Review();
            data.rid = review.id;
            //setOnClickListener(v -> Navigator.goReviewDetail(context, data));
        }
    }
}
