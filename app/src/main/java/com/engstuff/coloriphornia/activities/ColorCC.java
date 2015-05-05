package com.engstuff.coloriphornia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.data.Cv;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.SeekBarsColorControlFragment;
import com.engstuff.coloriphornia.helpers.AppHelper;
import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.helpers.PrefsHelper;

public class ColorCC extends BaseColorActivity {

    private FragmentColorBox fragmentColorBox2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentControl = new SeekBarsColorControlFragment();
        fragmentColorBox2 = new FragmentColorBox();

        getFragmentManager().beginTransaction()

                .add(R.id.color_control_container, fragmentControl)
                .add(R.id.color_box_container, fragmentColorBox)
                .add(R.id.color_box_container2, fragmentColorBox2)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        registerForContextMenu(fragmentColorBox2.getView());

        String hexColor1 = PrefsHelper.readFromPrefsString(
                this, Cv.PREFS_RETAIN, Cv.LAST_COLOR_BOX_1);

        if (!checkHexColorString(hexColor1)) {

            fragmentControl.setControls(255, 0, 255, 0);
            onColorControlChange(0, 0);

            fragmentColorBox2.setColorParams(255, 0, 0, 255).changeColor();
        } else {

            int[] argb = ColorParams.hexStringToARGB(hexColor1);

            fragmentControl.setControls(argb[0], argb[1], argb[2], argb[3]);
            onColorControlChange(0, 0);

            String hexColor2 = PrefsHelper.readFromPrefsString(
                    this, Cv.PREFS_RETAIN, Cv.LAST_COLOR_BOX_2);

            if (checkHexColorString(hexColor2)) {
                fragmentColorBox2.setColorParams(hexColor2).changeColor();
            }
        }
        AppHelper.setLikesAndInfo(this, fragmentColorBox, fragmentColorBox2);

        unlockInfo = true;
    }

    private boolean checkHexColorString(String hexColor2) {
        return hexColor2.startsWith("#") && (hexColor2.length() == 7 || hexColor2.length() == 9);
    }

    @Override
    public void onPause() {
        super.onPause();

            PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, Cv.LAST_COLOR_BOX_1,
                    fragmentColorBox.getHexColorParams());

            PrefsHelper.writeToPrefs(this, Cv.PREFS_RETAIN, Cv.LAST_COLOR_BOX_2,
                    fragmentColorBox2.getHexColorParams());

        unlockInfo = false;
    }

    @Override
    public void onInfoClicked(FragmentColorBox box) {

        fullColorStarted = true;

        changeFragment(box);

        String colorParams = box.getHexColorParams();

        Intent i = new Intent(this, FullScreenColorCC.class);
        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, box.isWhiteText());

        int fragmentId = box.getId() == R.id.color_box_container
                ? R.id.color_box_container2 : R.id.color_box_container;

        box = (FragmentColorBox) getFragmentManager().findFragmentById(fragmentId);

        String colorParams2 = box.getHexColorParams();

        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_2, colorParams2);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_2, box.isWhiteText());

        startActivity(i);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_color_cc;
    }

    protected void changeFragment(FragmentColorBox box) {

        unlockInfo = false;

        currentColorBox = box;
        fragmentControl.setControls(
                box.getAlpha(), box.getR(), box.getG(), box.getB());
        AppHelper.setInfoIcon(box);

        unlockInfo = true;
    }
}