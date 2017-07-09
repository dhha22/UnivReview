package com.univreview.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.model.Review;
import com.univreview.model.ReviewLike;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.TimeUtil;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.univreview.App.context;

/**
 * Created by DavidHa on 2017. 6. 18..
 */

public class ReviewDetailHeader extends FrameLayout {
    @BindView(R.id.subject_professor) TextView subjectProfessorTxt;
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.auth_mark) TextView authMark;
    @BindView(R.id.time_txt) TextView timeTxt;
    @BindView(R.id.more_btn) ImageButton moreBtn;
    @BindView(R.id.review_rating_indicator_view) ReviewRatingIndicatorView reviewRatingIndicatorView;
    @BindView(R.id.review_detail_layout) LinearLayout reviewDetailLayout;
    @BindView(R.id.review_detail_txt) TextView reviewDetailTxt;
    @BindView(R.id.like_layout) LinearLayout likeLayout;
    @BindView(R.id.like_cnt) TextView likeCntTxt;
    @BindView(R.id.comment_cnt) TextView commentCntTxt;
    @BindView(R.id.like_img) ImageView likeImg;
    private Context context;
    private Review data;

    public ReviewDetailHeader(@NonNull Context context) {
        this(context, null);
    }

    public ReviewDetailHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReviewDetailHeader(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.review_detail_header, this, true);
        this.context = context;
        ButterKnife.bind(this);

    }


    public void setData(Review data){
        this.data = data;
        nameTxt.setText(data.user.name);
        if (data.user.authenticated != null) {
            if (data.user.authenticated) {
                authMark.setVisibility(View.VISIBLE);
            } else {
                authMark.setVisibility(View.GONE);
            }
        }else{
            authMark.setVisibility(View.GONE);
        }

        likeImg.setSelected(data.likes);
        likeCntTxt.setText(data.likeCount+"명");
        commentCntTxt.setText(data.commentCount+"명");
        likeLayout.setOnClickListener(v -> {
            callReviewLike(data.id);
            if (likeImg.isSelected()) {
                likeImg.setSelected(false);
            } else {
                likeImg.setSelected(true);
            }
        });

        SpannableStringBuilder builder = new SpannableStringBuilder();
        int index = 0;
        if (data.subjectDetail.subject != null) {
            builder.append(data.subjectDetail.subject.getName() + " ");
        }
        Util.addSizeSpan(builder, index, Util.dpToPx(context, 16));
        Util.addColorSpan(context, builder, index, R.color.colorPrimary);
        index = builder.length();

        if (data.subjectDetail.professor != null) {
            builder.append(data.subjectDetail.professor.getName() + " 교수님");
        }
        Util.addSizeSpan(builder, index, Util.dpToPx(context, 14));
        Util.addColorSpan(context, builder, index, R.color.professorTxtColor);

        subjectProfessorTxt.setText(builder);

        if (data.reviewDetail != null) {
            reviewDetailTxt.setText(data.reviewDetail.reviewDetail);
            reviewDetailLayout.setVisibility(View.VISIBLE);
        }

        timeTxt.setText(new TimeUtil().getPointFormat(data.createdDate));
        reviewRatingIndicatorView.setData(data);
    }

    public void setMoreBtnOnClickListener(OnClickListener clickListener){
        moreBtn.setOnClickListener(clickListener);
    }

    private void callReviewLike(long id) {
        Retro.instance.reviewService().likeReview(App.setAuthHeader(App.userToken), new ReviewLike(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (data.likes) {
                        data.likes = false;
                        data.likeCount--;
                    } else {
                        data.likes = true;
                        data.likeCount++;
                    }
                    setData(data);
                }, ErrorUtils::parseError);
    }
}