package com.rodvar.mvppet.presentation;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.rodvar.mvppet.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusActivity;

/**
 * Thanks to the usage of MVP, this view only codes handling of its views and population of data,
 * regardless where this data was taken from.
 * <p/>
 * RequiresPresenter ensure the presenter instance is not loss on a config change or activity restart
 */
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusActivity<MainPresenter> {

    @Bind(R.id.txtContent)
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.txt.setText("Nucleus rules!");
    }

    @OnClick(R.id.txtContent)
    public void onClickTxt() {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    }

    public void onModelFetchSuccess(Object data) {
        Toast.makeText(this, "Do sth on success!", Toast.LENGTH_SHORT).show();
    }

    public void onModelFetchError(Throwable error) {
        Toast.makeText(this, "Do sth on error! " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
