package com.rodvar.mvppet.presentation;

import android.os.Bundle;
import android.util.Log;

import com.rodvar.mvppet.R;

import nucleus.view.NucleusActionBarActivity;

/**
 * Main activity, delegates work to fragments.
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

    /**
     *
     * @param replaceResId
     * @param fragment
     */
    private void loadFragment(int replaceResId, MainFragment fragment) {
        try {
            this.getFragmentManager().beginTransaction().replace(replaceResId, fragment).commit();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Failed to load fragment", e);
        }
    }
}
