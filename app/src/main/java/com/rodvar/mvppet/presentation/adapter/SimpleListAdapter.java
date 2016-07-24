package com.rodvar.mvppet.presentation.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Based on nucleus-example-real-life project
 *
 * @param <T>
 */
public class SimpleListAdapter<T> extends BaseProgressAdapter {

    private List<T> items = Collections.emptyList();

    public SimpleListAdapter(ViewHolderType... types) {
        super(0, types);
    }

    public SimpleListAdapter(int progressViewId, ViewHolderType... types) {
        super(progressViewId, types);
    }

    public void set(List<T> items) {
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        notifyDataSetChanged();
    }

    public void add(List<T> items) {
        int prevSize = this.items.size();
        List<T> list = new ArrayList<>(prevSize + items.size());
        list.addAll(this.items);
        list.addAll(items);
        this.items = Collections.unmodifiableList(list);
        notifyItemRangeInserted(prevSize, items.size());
    }

    public void clear() {
        items = Collections.emptyList();
        notifyDataSetChanged();
    }

    @Override
    protected List<T> getItems() {
        return items;
    }

    public T get(int position) {
        return items.get(position);
    }
}

