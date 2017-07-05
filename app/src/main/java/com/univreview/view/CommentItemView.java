package com.univreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.univreview.R;
import com.univreview.model.ReviewComment;
import com.univreview.util.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 5. 17..
 */
public class CommentItemView extends FrameLayout{
    @BindView(R.id.reply_img) ImageView replyImage;
    @BindView(R.id.name_txt) TextView nameTxt;
    @BindView(R.id.message_txt) TextView messageTxt;
    @BindView(R.id.reply_txt) TextView replyTxt;
    @BindView(R.id.time_txt) TextView timeTxt;

    public CommentItemView(Context context) {
        this(context, null);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.comment_item, this, true);
        ButterKnife.bind(this);
    }

    public void setData(ReviewComment data){
        nameTxt.setText(data.name);
        messageTxt.setText(data.commentDetail);
        timeTxt.setText(new TimeUtil().getPointFormat(data.createdDate));
        if(data.parentCommentId != null){
            replyImage.setVisibility(VISIBLE);
            replyTxt.setVisibility(GONE);
        }else{
            replyImage.setVisibility(GONE);
            replyTxt.setVisibility(VISIBLE);
        }
    }
}
