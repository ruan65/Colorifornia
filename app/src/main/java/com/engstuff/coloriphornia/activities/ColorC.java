package com.engstuff.coloriphornia.activities;

import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentSeekBarsControl;
import com.engstuff.coloriphornia.helpers.HexColorFrom4parts;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public class ColorC extends BaseColorActivity
        implements FragmentSeekBarsControl.ColorControlChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()

                .add(R.id.color_control_container, fragmentControl)
                .add(R.id.color_box_container, fragmentColorBox)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String hexColor = PrefsHelper.readFromPrefsString(
                this, Cv.PREFS_RETAIN, "last_color");

        if (hexColor.equals("")) {

            fragmentControl.setControls(255, 255, 0, 0);
            currentColorBox.setColorParams().changeColor();

        } else {

            int[] argb = HexColorFrom4parts.hexStringToARGB(hexColor);

            fragmentControl.setControls(argb[0], argb[1], argb[2], argb[3]);
            currentColorBox.setColorParams().changeColor();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, "last_color",
                currentColorBox.getHexColorParams());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.color_c;
    }
}
