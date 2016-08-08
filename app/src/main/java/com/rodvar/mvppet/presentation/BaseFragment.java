package com.rodvar.mvppet.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rodvar.mvppet.presentation.adapter.IBaseProgressAdapter;

import butterknife.ButterKnife;
import nucleus.view.NucleusFragment;

/**
 * Created by rodrigovarela on 8/08/2016.
 * Base class for every Fragment of the app
 * T presenter
 * D data model type
 */
public abstract class BaseFragment<P extends BasePresenter, D> extends NucleusFragment<P> {

    private boolean readyToLoadMoreData;

    /**
     * What to do on request succeded
     * @param items
     */
    protected abstract void doOnRequestSucceded(D[] items);

    /**
     * What to do on request failed
     * @param throwable
     */
    protected abstract void doOnRequestFailed(Throwable throwable);

    /**
     * Main adapter to display data
     * @return
     */
    protected abstract IBaseProgressAdapter getMainAdapter();

    /**
     * What to show when there is no data available
     * @return
     */
    protected abstract View getEmptyView();

    /**
     * Main recycler view to show data
     * @return
     */
    protected abstract View getMainRecyclerView();

    /**
     * Specific things to do on create view
     * @param rootView
     */
    protected abstract void doOnCreateView(View rootView);

    /**
     * res id for the current fragment view
     * @return
     */
    protected abstract int getFragmentLayoutResId();

    /**
     *
     * @return true if this view can load more that, false otherwise
     */
    protected boolean isReadyToLoadMoreData() {
        return readyToLoadMoreData;
    }

    /**
     *
     * @param readyToLoadMoreData
     */
    public void setReadyToLoadMoreData(boolean readyToLoadMoreData) {
        this.readyToLoadMoreData = readyToLoadMoreData;
    }

    /**
     * Auto binds with butterknife
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.getFragmentLayoutResId(), container, false);
        ButterKnife.bind(this, view);
        this.doOnCreateView(view);
        this.requestData();
        return view;
    }

    /**
     * Auto unbinding
     */
    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }


    /**
     * Request more data to the presenter
     */
    protected void requestData() {
        this.showLoading(true);
        this.getPresenter().request();
    }


    public void onRequestSuccess(D[] items) {
        this.doOnRequestSucceded(items);
        this.showLoading(false);
        this.viewData(true);
        this.readyToLoadMoreData = true;
    }

    public void onNetworkError(Throwable throwable) {
        this.doOnRequestFailed(throwable);
        this.showLoading(false);
        this.viewData(false);
        this.readyToLoadMoreData = true;
    }

    private void showLoading(boolean loading) {
        if (loading)
            this.getMainAdapter().showProgress();
        else
            this.getMainAdapter().hideProgress();
    }

    private void viewData(boolean showData) {
        this.getEmptyView().setVisibility(!showData ? View.VISIBLE : View.GONE);
        this.getMainRecyclerView().setVisibility(showData ? View.VISIBLE : View.GONE);
    }

}
