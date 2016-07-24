package com.rodvar.mvppet.presentation.adapter;

import android.view.ViewGroup;

/**
 * based on nucleus-example-real-life
 *
 * @param <T>
 */
public interface ViewHolderType<T> {
    boolean isOfItem(Object item);

    BaseViewHolder<T> create(ViewGroup parent);
}
