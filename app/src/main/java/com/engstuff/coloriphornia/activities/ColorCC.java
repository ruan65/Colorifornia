package com.engstuff.coloriphornia.activities;

import android.content.Intent;
import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.FragmentSeekBarsControl;
import com.engstuff.coloriphornia.helpers.HexColorFrom4parts;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public class ColorCC extends BaseColorActivity {

    FragmentColorBox fragmentColorBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentColorBox2 = new FragmentColorBox();

        getFragmentManager().beginTransaction()

                .add(R.id.color_control_container, fragmentControl)
                .add(R.id.color_box_container, fragmentColorBox)
                .add(R.id.color_box_container2, fragmentColorBox2)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerForContextMenu(fragmentColorBox2.getView());

        String hexColor1 = PrefsHelper.readFromPrefsString(
                this, Cv.PREFS_RETAIN, "last_color_box1");

        if (!checkHexColorString(hexColor1)) {

            fragmentControl.setControls(255, 0, 255, 0);
            onColorControlChange();

            fragmentColorBox2.setColorParams(255, 0, 0, 255).changeColor();
        } else {

            int[] argb = HexColorFrom4parts.hexStringToARGB(hexColor1);

            fragmentControl.setControls(argb[0], argb[1], argb[2], argb[3]);
            onColorControlChange();

            String hexColor2 = PrefsHelper.readFromPrefsString(
                    this, Cv.PREFS_RETAIN, "last_color_box2");

            if (checkHexColorString(hexColor2)) {
                fragmentColorBox2.setColorParams(hexColor2).changeColor();
            }
        }

    }

    private boolean checkHexColorString(String hexColor2) {
        return hexColor2.startsWith("#") && (hexColor2.length() == 7 || hexColor2.length() == 9);
    }

    @Override
    protected void onPause() {
        super.onPause();

        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, "last_color_box1",
                fragmentColorBox.getHexColorParams());

        PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, "last_color_box2",
                fragmentColorBox2.getHexColorParams());
    }

    @Override
    public void onInfoClicked(FragmentColorBox color) {

        changeFragment(color);

        String[] colorParams = {
                color.getRgbColorParams(),
                color.getHexColorParams()
        };

        Intent i = new Intent(this, FullScreenColorCC.class);
        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, color.isWhiteText());

        int fragmentId = color.getId() == R.id.color_box_container
                ? R.id.color_box_container2 : R.id.color_box_container;

        color = (FragmentColorBox) getFragmentManager().findFragmentById(fragmentId);

        String[] colorParams2 = {
                color.getRgbColorParams(),
                color.getHexColorParams()
        };

        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_2, colorParams2);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_2, color.isWhiteText());

        startActivity(i);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.color_cc;
    }

    protected void changeFragment(FragmentColorBox color) {
        currentColorBox = color;
        fragmentControl.setControls(
                color.getAlpha(), color.getR(), color.getG(), color.getB());
    }
}


