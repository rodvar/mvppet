package com.rodvar.mvppet;

import android.os.Bundle;

import nucleus.view.NucleusActivity;

public class MainActivity extends NucleusActivity<MainPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
