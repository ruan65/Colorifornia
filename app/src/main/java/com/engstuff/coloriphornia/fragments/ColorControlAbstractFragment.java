package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.util.Log;

import com.engstuff.coloriphornia.helpers.ColorParams;
import com.engstuff.coloriphornia.interfaces.ColorControlChangeListener;

public abstract class ColorControlAbstractFragment extends Fragment {

    protected ColorControlChangeListener colorChangeListener;

    protected int alpha, r, g, b; // alpha, red, green, blue

    protected ColorControlAbstractFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            colorChangeListener = (ColorControlChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnColorControlChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        colorChangeListener = null;
    }

    public abstract void setControls(int alpha, int r, int g, int b);

    public void setControls(int color) {

        setControls(Color.alpha(color), Color.red(color), Color.green(color), color & 0xff);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getColor() {
        return ColorParams.composeHex(alpha, r, g, b);
    }
}
