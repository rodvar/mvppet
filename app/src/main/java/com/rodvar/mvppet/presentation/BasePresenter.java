package com.rodvar.mvppet.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import icepick.Icepick;
import nucleus.presenter.RxPresenter;

/**
 * Created by rodrigovarela on 8/08/2016.
 *
 * F = fragment
 * D = data model
 *
 * Has support for a main request build on #initAPICall() and executed on #request()
 *
 * Responsible for:
 * - Getting the data needed for the recyclerView
 * - passing the data or informing error to view
 */
public abstract class BasePresenter<F extends BaseFragment, D> extends RxPresenter<F> {

    private F view;
    private D[] data;

    private Throwable error;


    /**
     * Creates the main api call asociated with a restartable id (rx presenter)
     * Choose the restartable strategy and the call backs
     */
    protected abstract void initAPICall();

    /**
     * Execute server request
     */
    public void request() {
        Log.d(getClass().getSimpleName(), "Executing request to server");
        this.start(getMainRequestId());
    }

    /**
     *
     * @return main request ID
     */
    protected abstract int getMainRequestId();

    /**
     * Icepick restore state. Do call if necessary
     * @param savedState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        Icepick.restoreInstanceState(this, savedState);

        this.initAPICall();
        if (savedState == null)
            this.request();
    }

    /**
     * IcePick save state
     * @param state
     */
    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }

    /**
     * @param view recyclerView that needs presentation
     */
    @Override
    public void onTakeView(F view) {
        super.onTakeView(view);
        this.view = view;
        this.publish();
    }

    /**
     * Publish data to the recyclerView or indicates error if its the case
     */
    protected void publish() {
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

    public void setData(D[] data) {
        this.data = data;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
