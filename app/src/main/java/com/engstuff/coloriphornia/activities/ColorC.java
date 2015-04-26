package com.engstuff.coloriphornia.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.SeekBarsColorControlFragment;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public class ColorC extends BaseColorActivity {

    private TextView progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentControl = new SeekBarsColorControlFragment();

        getFragmentManager().beginTransaction()

                .add(R.id.color_control_container, fragmentControl)
                .add(R.id.color_box_container, fragmentColorBox)
                .commit();

        progress = (TextView) findViewById(R.id.sb_progress);
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

        progress.setVisibility(View.VISIBLE);

        String val = String.valueOf(p);

        switch (id) {

            case R.id.sbAlpha:
                progress.setTextColor(Color.BLACK);
                progress.setText(getString(R.string.alpha) + val);
                break;
            case R.id.sbRed:
                progress.setTextColor(Color.RED);
                progress.setText(getString(R.string.red) + val);
                break;
            case R.id.sbGreen:
                progress.setTextColor(getResources().getColor(R.color.dk_green));
                progress.setText(getString(R.string.green) + val);
                break;
            case R.id.sbBlue:
                progress.setTextColor(Color.BLUE);
                progress.setText(getString(R.string.blue) + val);
                break;
        }
    }

    @Override
    public void onColorControlStopTracking() {
        super.onColorControlStopTracking();

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(800);
        progress.setAnimation(anim);
        progress.setVisibility(View.INVISIBLE);
    }
}
