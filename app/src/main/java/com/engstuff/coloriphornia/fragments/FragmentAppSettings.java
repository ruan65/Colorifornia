package com.engstuff.coloriphornia.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.engstuff.coloriphornia.R;

public class FragmentAppSettings extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
