package com.engstuff.coloriphornia.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.FragmentNavDrawer;

public abstract class MockUpActivity extends ActionBarActivity {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout; // parent activity layout
    View mDrawerView; // child drawer view

    protected final Context ctx = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        FragmentNavDrawer drawerFragment = (FragmentNavDrawer) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_nav_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerView = findViewById(R.id.fragment_nav_drawer);
        drawerFragment.setUp(mDrawerLayout, mToolbar);
    }

    protected abstract int getLayoutResource();
}
