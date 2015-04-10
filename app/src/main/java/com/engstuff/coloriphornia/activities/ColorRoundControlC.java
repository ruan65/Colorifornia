package com.engstuff.coloriphornia.activities;

import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.RoundColorControlFragment;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public class ColorRoundControlC extends BaseColorActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentControl = new RoundColorControlFragment();

        getFragmentManager().beginTransaction()

                .add(R.id.color_control_container_round, fragmentControl)
                .add(R.id.color_box_container_round, fragmentColorBox)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        String hexColor = PrefsHelper.readFromPrefsString(
                this, Cv.PREFS_RETAIN, "last_color");

        if (hexColor.equals("")) {

            fragmentControl.setControls(255, 255, 0, 0);
            currentColorBox.setColorParams().changeColor();

        } else {

            int[] argb = ColorParams.hexStringToARGB(hexColor);

            fragmentControl.setControls(argb[0], argb[1], argb[2], argb[3]);
            currentColorBox.setColorParams().changeColor();
        }
        AppHelper.setLikesAndInfo(this, currentColorBox);
    }

    @Override
    public void onPause() {
        super.onPause();
        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, "last_color",
                currentColorBox.getHexColorParams());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_color_round_control;
    }
}
