package com.rodvar.mvppet.presentation.adapter;

import android.view.View;
import android.widget.TextView;

import com.rodvar.mvppet.R;

import rx.functions.Action1;

public class SimpleViewHolder<T> extends BaseViewHolder<T> {

    private final TextView textView;
    private T item;

    public SimpleViewHolder(View view, final Action1<T> onClick) {
        super(view);
        this.textView = (TextView) view.findViewById(R.id.text1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.call(item);
            }
        });
    }

    @Override
    public void bind(T item) {
        this.item = item;
        textView.setText(item.toString());
    }
}
