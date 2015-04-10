package com.engstuff.coloriphornia.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.components.views.RoundColorMaker;
import com.engstuff.coloriphornia.interfaces.RoundColorMakerChangedListener;

public class RoundColorControlFragment extends ColorControlAbstractFragment
        implements RoundColorMakerChangedListener {

    private RoundColorMaker roundControl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_round_control, container, false);

        roundControl = (RoundColorMaker) rootView.findViewById(R.id.view_round_color_maker);
        if (roundControl != null) {
            Log.d("ml", "setting round control listener: " + roundControl);
            roundControl.setRoundColorMakerChangedListener(this);
        }
        return rootView;
    }

    @Override
    public void onColorChanged(int val, int alpha) {

        this.alpha = alpha;
        r = Color.red(val);
        g = Color.green(val);
        b = Color.blue(val);

        Log.i("ml", "onColorChanged in the RoundColorControlFragment " + r + " " + g + " " + b);

        colorChangeListener.onColorControlChange();
    }

    @Override
    public void onDismiss(int val, int alpha) {

    }

    @Override
    public void setControls(int alpha, int r, int g, int b) {

    }


}
