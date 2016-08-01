package com.rodvar.mvppet.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rodvar.mvppet.LetsApp;
import com.rodvar.mvppet.data.network.ServerAPI;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;

/**
 * Created by rodrigo on 18/07/16.
 * Responsible for:
 * - Getting the data needed for the recyclerView
 * - passing the data or informing error to view
 */
public class MainPresenter extends RxPresenter<MainFragment> {

    private static final int REQUEST_ITEMS = 1;

    private MainFragment view;

    private ServerAPI.Item[] data;
    private Throwable error;

    public MainPresenter() {
        Log.d(getClass().getSimpleName(), "Presenter created");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        this.initAPICall();
        if (savedState == null)
            this.request();
    }

    private void initAPICall() {
        restartableLatestCache(REQUEST_ITEMS,
                new Func0<Observable<ServerAPI.Response>>() {
                    @Override
                    public Observable<ServerAPI.Response> call() {
                        return LetsApp.getServerAPI()
                                .getUpcomingEvents("Rocky", "Varela", 0)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                },
                new Action2<MainFragment, ServerAPI.Response>() {
                    @Override
                    public void call(MainFragment activity, ServerAPI.Response response) {
                        data = response.items;
                        publish();
                    }
                },
                new Action2<MainFragment, Throwable>() {
                    @Override
                    public void call(MainFragment activity, Throwable throwable) {
                        error = throwable;
                        publish();
                    }
                });
    }

    /**
     * @param view recyclerView that needs presentation
     */
    @Override
    public void onTakeView(MainFragment view) {
        super.onTakeView(view);
        this.view = view;
        this.publish();
    }

    /**
     * Publish data to the recyclerView or indicates error if its the case
     */
    private void publish() {
        if (view != null) {
            if (data != null) {
                Log.d(getClass().getSimpleName(), "Publish calling success callback..");
                view.onRequestSuccess(data);
            } else if (error != null) {
                Log.d(getClass().getSimpleName(), "Publish calling error callback..");
                view.onNetworkError(error);
            }
        }
    }

    public void request() {
        Log.d(getClass().getSimpleName(), "Executing request to server");
        start(REQUEST_ITEMS);
    }
}
