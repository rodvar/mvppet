package com.rodvar.mvppet.presentation;

import com.rodvar.mvppet.LetsApp;
import com.rodvar.mvppet.data.network.ServerAPI;

import icepick.State;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;

/**
 * Created by rodrigo on 18/07/16.
 */
public class MainPresenter extends BasePresenter<MainFragment, ServerAPI.Item> {

    private static final int ITEMS_REQUEST_ID = 1;

    @State String name = "Rocky";
    @State String surname = "Varela";

    @Override
    protected void initAPICall() {
        this.restartableLatestCache(this.getMainRequestId(),
                new Func0<Observable<ServerAPI.Response>>() {
                    @Override
                    public Observable<ServerAPI.Response> call() {
                        return LetsApp.getServerAPI()
                                .getUpcomingEvents(name, surname, 0)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                },
                new Action2<MainFragment, ServerAPI.Response>() {
                    @Override
                    public void call(MainFragment activity, ServerAPI.Response response) {
                        setData(response.items);
                        publish();
                    }
                },
                new Action2<MainFragment, Throwable>() {
                    @Override
                    public void call(MainFragment activity, Throwable throwable) {
                        setError(throwable);
                        publish();
                    }
                });
    }

    @Override
    protected int getMainRequestId() {
        return ITEMS_REQUEST_ID;
    }
}
