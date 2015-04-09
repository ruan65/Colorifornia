package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.app.Fragment;

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
}
