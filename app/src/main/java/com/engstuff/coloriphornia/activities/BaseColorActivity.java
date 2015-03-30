package com.engstuff.coloriphornia.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.DialogFragmentSavedEmails;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.FragmentNavDrawer;
import com.engstuff.coloriphornia.fragments.FragmentSeekBarsControl;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.engstuff.coloriphornia.helpers.PrefsHelper.eraseAllPrefs;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsAllToArray;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.readFromPrefsInt;
import static com.engstuff.coloriphornia.helpers.PrefsHelper.writeToPrefs;


public abstract class BaseColorActivity extends MockUpActivity
        implements FragmentColorBox.ColorBoxEventListener {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout; // parent activity layout
    View mDrawerView; // child drawer view

    protected final Context ctx = this;
    FragmentSeekBarsControl fragmentControl;
    protected FragmentColorBox fragmentColorBox;
    protected FragmentColorBox currentColorBox;

    List<WeakReference<Fragment>> allAttachedFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        fragmentControl = new FragmentSeekBarsControl();

        fragmentColorBox = currentColorBox = new FragmentColorBox();

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
    protected void onPause() {
        super.onPause();
        PrefsHelper.writeToPrefsDefault(
                getApplicationContext(), Cv.LAST_ACTIVITY, getClass().getName());
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        allAttachedFragments.add(new WeakReference<>(fragment));
    }

    protected abstract int getLayoutResource();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {

            case R.id.save_to_prefs:

                saveColorToPrefs();

                break;

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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void saveColorToPrefs() {

        String hexColorParams = currentColorBox.getHexColorParams();
        int colorHex = currentColorBox.getColorHex();

        writeToPrefs(this, Cv.SAVED_COLORS, hexColorParams, colorHex);

        if (readFromPrefsInt(this, Cv.SAVED_COLORS, hexColorParams) == colorHex) {

            Toast toast = new Toast(getApplicationContext());

            TextView view = (TextView)
                    getLayoutInflater().inflate(R.layout.toast_custom, null);

            view.setBackgroundColor(colorHex);

            view.setTextColor(currentColorBox.isWhiteText() ? Color.WHITE : Color.BLACK);
            view.setText("This color's been saved\n\n            " + hexColorParams);

            toast.setView(view);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

        }
    }

    private String composeEmailBody() {
        StringBuilder result = new StringBuilder()
                .append("<h3>Colors chosen via \"Colorifornia\" mobile app: </h3>");

        for (WeakReference<Fragment> ref : allAttachedFragments) {
            Fragment f = ref.get();
            if (f.getClass().equals(FragmentColorBox.class)) {
                result.append("<p>" + ((FragmentColorBox) f).getHexColorParams() + "</p>");
            }
        }

        return result.toString();
    }

    @Override
    public void onColorClicked(FragmentColorBox color) {

        changeFragment(color);
    }

    @Override
    public void onTextColorChanged(boolean white) {
        currentColorBox.getInfo().setImageResource(
                white ? R.drawable.ic_info_white
                      : R.drawable.ic_info_black);
    }

    protected void changeFragment(FragmentColorBox color) {
    }

    @Override
    public void onInfoClicked(FragmentColorBox color) {

        changeFragment(color);

        String[] colorParams = {
                color.getRgbColorParams(),
                color.getHexColorParams()
        };

        Intent i = new Intent(this, FullScreenColorC.class);

        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, fragmentColorBox.isWhiteText());

        startActivity(i);
    }

    public boolean isWhiteText() {
        return fragmentColorBox != null ? fragmentColorBox.isWhiteText() : false;
    }

    public FragmentSeekBarsControl getFragmentControl() {
        return fragmentControl;
    }
}
