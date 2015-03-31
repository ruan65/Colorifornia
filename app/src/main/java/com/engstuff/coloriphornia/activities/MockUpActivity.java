package com.engstuff.coloriphornia.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.DialogFragmentSavedEmails;
import com.engstuff.coloriphornia.fragments.FragmentNavDrawer;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.eraseAllPrefs;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAllToArray;

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

    @Override
    protected void onResume() {
        super.onResume();
        String keyNotFirstTime = getString(R.string.not_first_time);

        if (!PrefsHelper.readFromPrefsBoolean(this, keyNotFirstTime)) {
            mDrawerLayout.openDrawer(mDrawerView);
            PrefsHelper.writeToPrefsDefault(this, keyNotFirstTime, true);
        }
        overridePendingTransition(R.anim.slide_in_r, R.anim.slide_out_r);
    }

    @Override
    public void onPause() {
        super.onPause();
        PrefsHelper.writeToPrefsDefault(
                getApplicationContext(), Cv.LAST_ACTIVITY, getClass().getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.get_saved:

                for (String colorHexadecimal : readFromPrefsAllToArray(this, Cv.SAVED_COLORS)) {

                    Log.d("ml", "hex: " + colorHexadecimal);
                }
                break;

            case R.id.saved_emails:

                new DialogFragmentSavedEmails().show(getFragmentManager(), null);
                break;

            case R.id.erase:

                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Delete saved colors")
                        .setMessage("All saved colors will be deleted!?")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eraseAllPrefs(ctx, Cv.SAVED_COLORS);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ignore
                            }
                        })
                        .show();
                break;

            case R.id.menu_item_share:

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setType("message/rfc822");

                emailIntent.putExtra(Intent.EXTRA_EMAIL,
                        readFromPrefsAllToArray(this, Cv.SAVED_EMAILS));

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Color parameters from Colorifornia");

                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(composeEmailBody()));

                startActivity(Intent.createChooser(emailIntent, "Send color(s) parameters..."));

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getLayoutResource();

    protected abstract String composeEmailBody();

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
