package com.univreview.fragment.upload;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.Navigator;
import com.univreview.R;
import com.univreview.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 1. 8..
 */
public class UploadReviewFragment extends BaseFragment {
    @BindView(R.id.subject_txt) TextView subjectTxt;
    @BindView(R.id.professor_txt) TextView professorTxt;
    private Context context;

    public static UploadReviewFragment newInstance(){
        UploadReviewFragment fragment = new UploadReviewFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_upload_review, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();

        subjectTxt.setOnClickListener(v -> Navigator.goSearch(context, "subject", App.UNIVERSITY_ID));
        rootLayout.addView(view);
        return rootLayout;
    }
}
