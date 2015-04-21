package com.engstuff.coloriphornia.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentNavDrawer;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public abstract class MockUpActivity extends ActionBarActivity {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout; // parent activity layout
    View mDrawerView; // child drawer view
    MenuItem binIcon, checkModeIcon, undoIcon, sendIcon;

    protected final Activity activity = this;

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

    @Override
    protected void onResume() {
        super.onResume();
        String keyNotFirstTime = getString(R.string.not_first_time);

        if (!PrefsHelper.readFromPrefsBoolean(this, keyNotFirstTime)) {
            mDrawerLayout.openDrawer(mDrawerView);
            PrefsHelper.writeToPrefsDefault(this, keyNotFirstTime, true);

            try {
                PrefsHelper.writeToPrefs(activity, Cv.SAVED_EMAILS, AppHelper.getDeviceGoogleEmail(activity), null);
            } catch (Exception ignoreCosDoesNotMatter) {
            }

        } else if (mDrawerLayout.isDrawerOpen(mDrawerView)) {
            mDrawerLayout.closeDrawer(mDrawerView);
        }
        overridePendingTransition(R.anim.slide_in_r, R.anim.slide_out_r);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getClass() != AppSettingsActivity.class) {
            PrefsHelper.writeToPrefsDefault(
                    getApplicationContext(), Cv.LAST_ACTIVITY, getClass().getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        binIcon = menu.findItem(R.id.bin);
        checkModeIcon = menu.findItem(R.id.check_mode);
        undoIcon = menu.findItem(R.id.undo);
        sendIcon = menu.findItem(R.id.send);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.send:

                AppHelper.fireShareIntent(this, composeEmailBody(true));
                break;

            case R.id.back:

                AppHelper.startLastSavedActivity(this);
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getLayoutResource();

    protected abstract String composeEmailBody(boolean calledFromContextMenu);

    /**
     * This is for preventing app crash after pressing hardware Menu button
     * And now more - open/close drawer after pressing it
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {

            if (!mDrawerLayout.isDrawerOpen(mDrawerView)) {
                mDrawerLayout.openDrawer(mDrawerView);
            } else if (mDrawerLayout.isDrawerOpen(mDrawerView)) {
                mDrawerLayout.closeDrawer(mDrawerView);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
