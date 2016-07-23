package com.rodvar.mvppet.presentation;

import android.util.Log;

import nucleus.presenter.Presenter;

/**
 * Created by rodrigo on 18/07/16.
 * Responsible for:
 * - Getting the data needed for the view
 * - passing the data or informing error to the view
 */
public class MainPresenter extends Presenter<MainActivity> {

    private MainActivity view;

    private Object data;
    private Throwable error;

    public MainPresenter() {
        // TODO query RxJava
        Log.d(getClass().getSimpleName(), "Presenter created");
    }

    /**
     * @param view view that needs presentation
     */
    @Override
    public void onTakeView(MainActivity view) {
        super.onTakeView(view);
        this.view = view;
        this.publish();
    }

    /**
     * Publish data to the view or indicates error if its the case
     */
    private void publish() {
        if (view != null) {
            if (data != null)
                view.onModelFetchSuccess(data);
            else if (error != null)
                view.onModelFetchError(error);
        }
    }
}
