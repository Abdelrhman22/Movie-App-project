package com.example.android.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Abd elrhman Arafa on 29/08/2015.
 */
public class MainActivity extends ActionBarActivity {

    boolean twoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // in case tablet will be true , in case phone will be false
        twoPane = getResources().getBoolean(R.bool.twopane);
        if (twoPane) {
            setContentView(R.layout.two_pane_activity);
            replaceCurrentFragment(R.id.listFram, GridViewActivity.getInstance(twoPane)
                    , GridViewActivity.class.getName(), true, false);

        } else {
            setContentView(R.layout.one_pane_main_activity);
            replaceCurrentFragment(R.id.listFram, GridViewActivity.getInstance(twoPane)
                    , GridViewActivity.class.getName(), true, true);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return   ((GridViewActivity) getSupportFragmentManager().findFragmentByTag( GridViewActivity.class.getName())).onOptionsItemSelected(item);
    }
    // replcase with specific container
    public void replaceCurrentFragment(int container, Fragment targetFragment,
                                       String tag, boolean addToBackStack, boolean animate) {

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = false;
        fragmentPopped = manager.popBackStackImmediate(tag, 0);

        if (!fragmentPopped && manager.findFragmentByTag(tag) == null) { // fragment

            FragmentTransaction ft = manager.beginTransaction();
            if (animate)
                ft.setCustomAnimations(R.anim.slide_out_right,
                        R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right);
            ft.replace(container, targetFragment, tag);
            if (addToBackStack) {
                ft.addToBackStack(tag);
            }
            ft.commit();

            // ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {

        if (twoPane) {
            finish();
            super.onBackPressed();
        }
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
              //  getSupportFragmentManager().popBackStack();
                super.onBackPressed();
            } else {
                finish();
                //super.onBackPressed();
            }
        }

    }
}
