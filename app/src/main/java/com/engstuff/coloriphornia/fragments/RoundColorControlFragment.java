package com.engstuff.coloriphornia.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.components.views.RoundColorMaker;
import com.engstuff.coloriphornia.interfaces.ColorControlChangeListener;
import com.software.shell.fab.ActionButton;

public class RoundColorControlFragment extends ColorControlAbstractFragment
        implements ColorControlChangeListener {

    private RoundColorMaker roundControl;

    ActionButton reset;
    RoundColorMaker control;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_round_control, container, false);

        control = (RoundColorMaker) rootView.findViewById(R.id.view_round_color_maker);

        reset = (ActionButton) rootView.findViewById(R.id.reset_alpha);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.resetAlphaAndSatur();
            }
        });

        roundControl = (RoundColorMaker) rootView.findViewById(R.id.view_round_color_maker);
        if (roundControl != null) {
            roundControl.setColorControlChangeListener(this);
        }
        reset.hide();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        reset.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        reset.hide();
    }

    @Override
    public void setControls(int alpha, int r, int g, int b) {

        roundControl.setColorAndControls(alpha, r, g, b);
    }

    @Override
    public void onColorControlChange() {
    }

    @Override
    public void onColorControlChange(int val, int alpha) {

        this.alpha = alpha;
        r = Color.red(val);
        g = Color.green(val);
        b = Color.blue(val);

        colorChangeListener.onColorControlChange();
    }

    @Override
    public void onColorControlStartTracking() {
        colorChangeListener.onColorControlStartTracking();
    }

    @Override
    public void onColorControlStopTracking() {
        colorChangeListener.onColorControlStopTracking();
    }

    public ActionButton getReset() {
        return reset;
    }
}
