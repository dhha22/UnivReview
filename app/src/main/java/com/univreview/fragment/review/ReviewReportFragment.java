package com.univreview.fragment.review;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.univreview.App;
import com.univreview.R;
import com.univreview.activity.BaseActivity;
import com.univreview.fragment.BaseWriteFragment;
import com.univreview.log.Logger;
import com.univreview.model.ReviewReport;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;
import com.univreview.util.SimpleButtonState;
import com.univreview.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 4. 12..
 */
public class ReviewReportFragment extends BaseWriteFragment {
    @BindView(R.id.radio_1) AppCompatRadioButton radio1;
    @BindView(R.id.radio_2) AppCompatRadioButton radio2;
    @BindView(R.id.radio_3) AppCompatRadioButton radio3;
    @BindView(R.id.input) EditText input;
    @BindView(R.id.button) Button button;
    private SimpleButtonState nextButtonState;
    private long reviewId;

    public static ReviewReportFragment newInstance(long reviewId){
        ReviewReportFragment fragment = new ReviewReportFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("reviewId", reviewId);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewId = getArguments().getLong("reviewId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_review_report, container, false);
        ButterKnife.bind(this, view);
        toolbar.setBackBtnVisibility(true);
        toolbar.setTitleTxt("신고하기");
        init();
        rootLayout.addView(view);
        return rootLayout;
    }

    private void init() {
        nextButtonState = new SimpleButtonState(getContext(), button);
        nextButtonState.setDrawable(R.drawable.rounded_primary_rect, R.drawable.fill_rounded_primary_rect);
        nextButtonState.setTxtColor(R.color.colorPrimary, R.color.white);
        radio1.setOnClickListener(view -> {
            Util.hideKeyboard(getContext(), input);
            nextButtonState.setButtonState(true);
        });
        radio2.setOnClickListener(view -> {
            Util.hideKeyboard(getContext(), input);
            nextButtonState.setButtonState(true);
        });
        radio3.setOnCheckedChangeListener((compoundButton, b) -> setInputFocusable(b));
        button.setOnClickListener(v -> {
            if (nextButtonState.getButtonState()) {
                registerReport();
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (radio3.isChecked()) {
                    nextButtonState.setButtonState(editable.length() > 0);
                }
            }
        });
    }

    private void setInputFocusable(boolean focusable){
        if(!focusable){
            input.setText(null);
        }else{
            nextButtonState.setButtonState(input.getText().length() != 0);
        }
        input.setFocusable(focusable);
        input.setFocusableInTouchMode(focusable);
    }

    private void registerReport() {
        progressDialog.show();
        Util.hideKeyboard(getContext(), input);
        String message = null;
        if (radio1.isChecked()) {
            message = "음란성 및 욕설 또는 허위사실 내용";
        } else if (radio2.isChecked()) {
            message = "부적합한 스팸 및 도배 내용";
        } else if (radio3.isChecked()) {
            message = input.getText().toString();
        }

        callReviewReportApi(message);
    }

    private void callReviewReportApi(String message){
        ReviewReport reviewReport = new ReviewReport(reviewId, message);
        Logger.v("post review report: " + reviewReport);
        Retro.instance.reviewService().postReviewReport(App.setAuthHeader(App.userToken), reviewReport)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    progressDialog.dismiss();
                    ((BaseActivity) getActivity()).setOnBackPressedListener(null);
                    getActivity().onBackPressed();
                })
                .subscribe(result -> Logger.v("result: " + result), ErrorUtils::parseError);
    }
}
