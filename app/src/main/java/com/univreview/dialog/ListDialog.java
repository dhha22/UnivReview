package com.univreview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.univreview.R;
import com.univreview.adapter.CustomAdapter;
import com.univreview.listener.OnItemClickListener;
import com.univreview.log.Logger;
import com.univreview.util.SimpleDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2017. 7. 9..
 */

public class ListDialog extends Dialog {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.cancel_button) TextView cancelBtn;
    private DialogAdapter adapter;
    private Context context;

    public ListDialog(@NonNull Context context, List<String> data, OnItemClickListener itemClickListener) {
        super(context);
        this.context = context;
        adapter = new DialogAdapter(context, data);
        adapter.setOnItemClickListener(itemClickListener);
        Logger.v("LisDialog Constructor");
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

        setContentView(R.layout.dialog_list);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        cancelBtn.setOnClickListener(v -> dismiss());
    }

    private class DialogAdapter extends CustomAdapter{
        private List<String> list;
        public DialogAdapter(Context context, List<String> list) {
            super(context);
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.simple_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder)holder).textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            private TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = ButterKnife.findById(itemView, R.id.text);
                textView.setOnClickListener(v -> {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                    dismiss();
                });
            }
        }
    }
}
