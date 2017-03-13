package com.univreview.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.univreview.App;
import com.univreview.R;
import com.univreview.log.Logger;
import com.univreview.model.UserTicket;
import com.univreview.network.Retro;
import com.univreview.util.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2017. 2. 27..
 */
public class PointListHeaderView extends CardView {
    @BindView(R.id.total_point_txt) TextView totalPointTxt;
    @BindView(R.id.buy_ticket_btn) Button buyTicketBtn;
    @BindView(R.id.ticket_layout) RelativeLayout ticketLayout;
    @BindView(R.id.ticket_name_txt) TextView ticketNameTxt;
    @BindView(R.id.ticket_time_txt) TextView ticketTimeTxt;
    public PointListHeaderView(Context context) {
        this(context, null);
    }

    public PointListHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointListHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.point_list_header, this, true);
        setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this);
        buyTicketBtn.setOnClickListener(v -> callBuyTicketApi());
    }

    public void setPoint(int totalPoint){
        totalPointTxt.setText(totalPoint+"P");
    }

    public void setUserTicket(UserTicket userTicket){
        Logger.v("user ticket: " + userTicket);
        if(userTicket != null){
            TimeUtil timeUtil = new TimeUtil();
            buyTicketBtn.setVisibility(GONE);
            ticketLayout.setVisibility(VISIBLE);
            ticketNameTxt.setText(userTicket.ticket.name);
            ticketTimeTxt.setText(timeUtil.getPointFormat(userTicket.ticket.term.startDate)
                    + " ~ " + timeUtil.getPointFormat(userTicket.ticket.term.endDate));
        } else {
            buyTicketBtn.setVisibility(VISIBLE);
            ticketLayout.setVisibility(GONE);
        }
    }

    private void callBuyTicketApi(){
        UserTicket userTicket = new UserTicket();
        userTicket.userId = App.userId;
        Retro.instance.userService().postUserTicket(App.setAuthHeader(App.userToken), userTicket)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response(userTicket), Logger::e);
    }

    private void response(UserTicket userTicket){
        Logger.v("result: " + userTicket);
    }
}
