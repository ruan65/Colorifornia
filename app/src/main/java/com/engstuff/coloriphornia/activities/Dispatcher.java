package com.engstuff.coloriphornia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public class Dispatcher extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<?> activityClass;
        try {
            activityClass = Class.forName(
                    PrefsHelper.readFromPrefsString(getApplicationContext(), Cv.LAST_ACTIVITY));
        } catch (ClassNotFoundException e) {
            activityClass = ColorFromImage.class;
        }
        startActivity(new Intent(this, activityClass));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
