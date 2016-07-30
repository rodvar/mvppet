package com.rodvar.mvppet.presentation;

import android.os.Bundle;

import com.rodvar.mvppet.R;

import nucleus.view.NucleusActionBarActivity;

/**
 * Thanks to the usage of MVP, this recyclerView only codes handling of its views and population of data,
 * regardless where this data was taken from.
 * <p/>
 * RequiresPresenter ensure the presenter instance is not loss on a config change or activity restart
 */
public class MainActivity extends NucleusActionBarActivity<MainPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.setSubTitle(this.getString(R.string.default_section_subtitle));
        this.loadFragment(R.id.main_fragment, MainFragment.newInstance());
    }

    public void setSubTitle(String subTitle) {
        this.getSupportActionBar().setSubtitle(subTitle);
    }

    private void loadFragment(int replaceResId, MainFragment fragment) {
        this.getFragmentManager().beginTransaction().replace(replaceResId, fragment).commit();
    }
}
