package com.engstuff.coloriphornia.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.AppSettingsActivity;
import com.engstuff.coloriphornia.activities.ColorC;
import com.engstuff.coloriphornia.activities.ColorCC;
import com.engstuff.coloriphornia.activities.ColorFromImage;
import com.engstuff.coloriphornia.activities.ColorRoundControlC;
import com.engstuff.coloriphornia.activities.FavoriteColorsActivity;
import com.engstuff.coloriphornia.activities.FontAndBackgroundActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentNavDrawer extends Fragment {

    private Activity activity;
    private ActionBarDrawerToggle mDrawerToggle;

    public FragmentNavDrawer() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_drawer_layout, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void setUp(DrawerLayout layout, Toolbar toolbar) {

        mDrawerToggle = new ActionBarDrawerToggle(
                activity, layout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();
            }
        };

        layout.setDrawerListener(mDrawerToggle);

        // very nice effect sandwich/rotate/arrow
        layout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    /**
     * Buttons
     */

    @OnClick(R.id.btn_two_colors)
    public void goTo2ColorsActivity() {
        startActivity(new Intent(activity, ColorCC.class));
    }

    @OnClick(R.id.btn_one_color)
    public void goTo1ColorsActivity() {
        startActivity(new Intent(activity, ColorC.class));
    }

    @OnClick(R.id.btn_img)
    public void goToImgActivity() {
        startActivity(new Intent(activity, ColorFromImage.class));
    }

    @OnClick(R.id.btn_favorite)
    public void goToFavoritesColors() {
        startActivity(new Intent(activity, FavoriteColorsActivity.class));
    }

    @OnClick(R.id.btn_round_control_c)
    public void goToRoundControlCActivity() {
        startActivity(new Intent(activity, ColorRoundControlC.class));
    }

    @OnClick(R.id.btn_settings)
    public void goToPrefs() {
        startActivity(new Intent(activity, AppSettingsActivity.class));
    }

    @OnClick(R.id.btn_font)
    public void goToFont() {
        startActivity(new Intent(activity, FontAndBackgroundActivity.class));
    }
}