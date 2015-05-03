package com.engstuff.coloriphornia.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.SeekBarsColorControlFragment;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public class ColorC extends BaseColorActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentControl = new SeekBarsColorControlFragment();

        getFragmentManager().beginTransaction()

                .add(R.id.color_control_container, fragmentControl)
                .add(R.id.color_box_container, fragmentColorBox)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        AppHelper.setColorToColorBox(this, Cv.LAST_COLOR, fragmentControl, currentColorBox);
        AppHelper.setLikesAndInfo(this, currentColorBox);
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, Cv.LAST_COLOR,
                currentColorBox.getHexColorParams());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_color_c;
    }

    @Override
    public void onColorControlChange(int p, int id) {
        super.onColorControlChange(p, id);

        switchInfo(p, id);
    }

    @Override
    public void onColorControlStopTracking() {
        super.onColorControlStopTracking();

        animInfoAndGone();
    }
}