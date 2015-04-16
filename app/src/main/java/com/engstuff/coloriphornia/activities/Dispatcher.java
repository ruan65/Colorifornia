package com.engstuff.coloriphornia.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.engstuff.coloriphornia.helpers.AppHelper;

public class Dispatcher extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppHelper.startLastSavedActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
