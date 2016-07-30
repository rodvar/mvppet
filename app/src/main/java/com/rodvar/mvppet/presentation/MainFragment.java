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
import com.rodvar.mvppet.presentation.adapter.BaseViewHolder;
import com.rodvar.mvppet.presentation.adapter.ClassViewHolderType;
import com.rodvar.mvppet.presentation.adapter.SimpleListAdapter;
import com.rodvar.mvppet.presentation.adapter.SimpleViewHolder;

import java.util.Arrays;

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
    RecyclerView recyclerView;
    @Bind(R.id.empty_view)
    TextView emptyView;
    private GridLayoutManager layoutManager;
    private SimpleListAdapter<ServerAPI.Item> adapter;

    /**
     * @return new MainFragment instance
     */
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private boolean canLoadMore;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            if (dy > 0) { //scroll down
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                if (canLoadMore) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        canLoadMore = false;
                        Log.v(getClass().getSimpleName(), "Request more data");
                        requestData();
                    }
                }
            }
        }
    };

    /**
     * Request more data to the presenter
     */
    private void requestData() {
        this.loadData(true);
        this.getPresenter().request();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        // decorate recycle recyclerView?
        this.layoutManager = new GridLayoutManager(this.getActivity(), 2);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.addOnScrollListener(this.scrollListener);
        this.adapter = new SimpleListAdapter<>(R.layout.recycler_view_progress,
                new ClassViewHolderType<>(ServerAPI.Item.class, R.layout.item, new ClassViewHolderType.ViewHolderFactory<ServerAPI.Item>() {
                    @Override
                    public BaseViewHolder<ServerAPI.Item> call(final View view) {
                        return new SimpleViewHolder<>(view, new Action1<ServerAPI.Item>() {
                            @Override
                            public void call(ServerAPI.Item item) {
                                Toast.makeText(getActivity(), "click " + item, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }));
        this.recyclerView.setAdapter(this.adapter);
        this.requestData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.recyclerView.removeOnScrollListener(this.scrollListener);
        ButterKnife.unbind(this);
    }

    /**
     * @param items
     */
    public void onRequestSuccess(ServerAPI.Item[] items) {
        this.adapter.add(Arrays.asList(items));
        this.loadData(false);
        this.viewData(true);
        this.canLoadMore = true;
    }

    public void onNetworkError(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        this.loadData(false);
        this.viewData(false);
    }

    private void loadData(boolean loading) {
        if (loading)
            this.adapter.showProgress();
        else
            this.adapter.hideProgress();
        //this.recyclerView.setVisibility(!loading ? View.VISIBLE : View.GONE);
    }

    private void viewData(boolean showData) {
        this.emptyView.setVisibility(!showData ? View.VISIBLE : View.GONE);
        this.recyclerView.setVisibility(showData ? View.VISIBLE : View.GONE);
    }
}
