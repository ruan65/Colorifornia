package com.engstuff.coloriphornia.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.fragments.FragmentColorBox;
import com.engstuff.coloriphornia.fragments.FragmentSeekBarsControl;

public class ColorC extends BaseActivity
        implements FragmentSeekBarsControl.ColorControlChangeListener, FragmentColorBox.ColorBoxEventListener {

    protected FragmentSeekBarsControl fragmentControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentControl = new FragmentSeekBarsControl();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction
                .add(R.id.color_control_container, fragmentControl)
                .add(R.id.color_box_container, fragmentColorBox)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.color_c;
    }

    @Override
    public void onColorControlChange() {
        currentColorBox
                .setColorParams()
                .changeColor();
    }

    public FragmentSeekBarsControl getFragmentControl() {
        return fragmentControl;
    }
}
