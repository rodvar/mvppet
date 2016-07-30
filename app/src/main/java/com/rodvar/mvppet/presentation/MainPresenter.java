package com.rodvar.mvppet.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rodvar.mvppet.LetsApp;
import com.rodvar.mvppet.domain.Event;

import java.util.List;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;

/**
 * Created by rodrigo on 18/07/16.
 * Responsible for:
 * - Getting the data needed for the view
 * - passing the data or informing error to the view
 */
public class MainPresenter extends RxPresenter<MainFragment> {

    private static final int REQUEST_ITEMS = 1;

    private MainFragment view;

    private List<Event> data;
    private Throwable error;

    public MainPresenter() {
        Log.d(getClass().getSimpleName(), "Presenter created");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        this.request();

        if (savedState == null)
            start(REQUEST_ITEMS);
    }

    /**
     * @param view view that needs presentation
     */
    @Override
    public void onTakeView(MainFragment view) {
        super.onTakeView(view);
        this.view = view;
        this.publish();
    }

    /**
     * Publish data to the view or indicates error if its the case
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

        restartableLatestCache(REQUEST_ITEMS,
                new Func0<Observable<List<Event>>>() {
                    @Override
                    public Observable<List<Event>> call() {
                        return LetsApp.getServerAPI()
                                .getUpcomingEvents()
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                },
                new Action2<MainFragment, List<Event>>() {
                    @Override
                    public void call(MainFragment activity, List<Event> response) {
                        data = response;
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
}
