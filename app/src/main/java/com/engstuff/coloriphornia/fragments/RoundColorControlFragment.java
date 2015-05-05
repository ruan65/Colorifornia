package com.engstuff.coloriphornia.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.components.views.RoundColorMaker;
import com.engstuff.coloriphornia.interfaces.ColorControlChangeListener;
import com.software.shell.fab.ActionButton;

public class RoundColorControlFragment extends ColorControlAbstractFragment
        implements ColorControlChangeListener {

    private RoundColorMaker roundControl;
    private ActionButton reset;
    private TextView colorInfo, colorInfoAlpha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_round_control, container, false);

        roundControl = (RoundColorMaker) rootView.findViewById(R.id.view_round_color_maker);

        colorInfo = (TextView) rootView.findViewById(R.id.round_info);

        colorInfoAlpha = (TextView) rootView.findViewById(R.id.round_info_alpha);

        reset = (ActionButton) rootView.findViewById(R.id.reset_alpha);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundControl.resetAlphaAndSatur();
            }
        });

        reset.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().setInterpolator(new DecelerateInterpolator())
                                .scaleX(.7f).scaleY(.7f);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.animate().setInterpolator(new OvershootInterpolator(5f))
                                .scaleX(1f).scaleY(1f);
                        break;
                }

                return false;
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
    public void onColorControlChange(int p, int id) {
    }

    @Override
    public void onColorControlChange(int progress, int val, int alpha, float[] hsv) {

        this.alpha = alpha;
        r = Color.red(val);
        g = Color.green(val);
        b = Color.blue(val);

        StringBuilder sb = new StringBuilder("hue: " + (int) hsv[0] + (char) 0x00B0);
        sb.append("\nsat: " + (int) (hsv[1] * 100) + (char) 0x0025);
        sb.append("\nval: " + (int) (hsv[2] * 100) + (char) 0x0025);

        colorInfo.setText(sb.toString());

        colorInfoAlpha.setText("opacity: " + alpha * 100 / 255 + (char) 0x0025);

        colorChangeListener.onColorControlChange(val, 0);
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