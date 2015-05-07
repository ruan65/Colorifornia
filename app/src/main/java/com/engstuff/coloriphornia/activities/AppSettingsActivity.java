package com.engstuff.coloriphornia.activities;

import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.FragmentAppSettings;

public class AppSettingsActivity extends MockUpActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .add(R.id.settings_fragment_container, new FragmentAppSettings())
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_app_settings;
    }

    @Override
    protected String composeEmailBody(boolean calledFromContextMenu, int fontColor) {
        return "";
    }

}
