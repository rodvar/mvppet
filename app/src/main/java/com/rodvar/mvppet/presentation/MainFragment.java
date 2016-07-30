package com.rodvar.mvppet.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rodvar.mvppet.R;
import com.rodvar.mvppet.data.network.ServerAPI;
import com.rodvar.mvppet.domain.Event;
import com.rodvar.mvppet.presentation.adapter.BaseViewHolder;
import com.rodvar.mvppet.presentation.adapter.ClassViewHolderType;
import com.rodvar.mvppet.presentation.adapter.SimpleListAdapter;
import com.rodvar.mvppet.presentation.adapter.SimpleViewHolder;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusFragment;
import rx.functions.Action1;

/**
 * Created by rodrigo on 23/07/16.
 * Shows the lists of events
 */
@RequiresPresenter(MainPresenter.class)
public class MainFragment extends NucleusFragment<MainPresenter> {

    @Bind(R.id.events_view)
    RecyclerView view;
    @Bind(R.id.empty_view)
    TextView emptyView;
    private GridLayoutManager layoutManager;
    private SimpleListAdapter<Event> adapter;

    /**
     * @return new MainFragment instance
     */
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null)
            this.getPresenter().request();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        // decorate recycle view?
        this.view.setHasFixedSize(true);
        this.layoutManager = new GridLayoutManager(this.getActivity(), 2);
        this.view.setLayoutManager(this.layoutManager);
        this.adapter = new SimpleListAdapter<>(R.layout.recycler_view_progress,
                new ClassViewHolderType<>(Event.class, R.layout.item, new ClassViewHolderType.ViewHolderFactory<Event>() {
                    @Override
                    public BaseViewHolder<Event> call(final View view) {
                        return new SimpleViewHolder<>(view, new Action1<Event>() {
                            @Override
                            public void call(Event item) {
                                Toast.makeText(getActivity(), "click " + item, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }));
        this.view.setAdapter(this.adapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * @param items
     */
    public void onRequestSuccess(List<Event> items) {
        this.adapter.set(items);
        this.viewData(true);
    }

    public void onNetworkError(Throwable throwable) {
        Log.e("NETWORK", "Error", throwable);
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        this.viewData(false);
    }

    private void viewData(boolean showData) {
        this.emptyView.setVisibility(!showData ? View.VISIBLE : View.GONE);
        this.view.setVisibility(showData ? View.VISIBLE : View.GONE);
    }
}
