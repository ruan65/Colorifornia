package com.engstuff.coloriphornia.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.components.views.SeekBarButton;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class SeekBarsColorControlFragment extends ColorControlAbstractFragment
        implements SeekBar.OnSeekBarChangeListener {

    private SeekBar sbAlpha, sbRed, sbGreen, sbBlue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seek_bars_control, container, false);

        sbAlpha = (SeekBar) rootView.findViewById(R.id.sbAlpha);
        sbRed = (SeekBar) rootView.findViewById(R.id.sbRed);
        sbRed = (SeekBar) rootView.findViewById(R.id.sbRed);
        sbGreen = (SeekBar) rootView.findViewById(R.id.sbGreen);
        sbBlue = (SeekBar) rootView.findViewById(R.id.sbBlue);

        sbAlpha.setOnSeekBarChangeListener(this);
        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);

        SeekBarButton mAlpha = (SeekBarButton) rootView.findViewById(R.id.minus_alpha);
        SeekBarButton mRed = (SeekBarButton) rootView.findViewById(R.id.minus_red);
        SeekBarButton mGreen = (SeekBarButton) rootView.findViewById(R.id.minus_green);
        SeekBarButton mBlue = (SeekBarButton) rootView.findViewById(R.id.minus_blue);

        SeekBarButton pAlpha = (SeekBarButton) rootView.findViewById(R.id.plus_alpha);
        SeekBarButton pRed = (SeekBarButton) rootView.findViewById(R.id.plus_red);
        SeekBarButton pGreen = (SeekBarButton) rootView.findViewById(R.id.plus_green);
        SeekBarButton pBlue = (SeekBarButton) rootView.findViewById(R.id.plus_blue);

        mAlpha.init(sbAlpha, colorChangeListener, false);
        mRed.init(sbRed, colorChangeListener, false);
        mGreen.init(sbGreen, colorChangeListener, false);
        mBlue.init(sbBlue, colorChangeListener, false);

        pAlpha.init(sbAlpha, colorChangeListener, true);
        pRed.init(sbRed, colorChangeListener, true);
        pGreen.init(sbGreen, colorChangeListener, true);
        pBlue.init(sbBlue, colorChangeListener, true);

        alpha = sbAlpha.getProgress();
        r = sbRed.getProgress(); g = sbGreen.getProgress(); b = sbBlue.getProgress();

        return rootView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        int id = seekBar.getId();

        switch (id) {
            case R.id.sbAlpha:
                alpha = progress;
                break;
            case R.id.sbRed:
                r = progress;
                break;
            case R.id.sbGreen:
                g = progress;
                break;
            case R.id.sbBlue:
                b = progress;
                break;
        }
        colorChangeListener.onColorControlChange(progress, id);
    }

    public void setControls(int a, int r, int g, int b) {
        sbAlpha.setProgress(a);
        sbRed.setProgress(r);
        sbGreen.setProgress(g);
        sbBlue.setProgress(b);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        colorChangeListener.onColorControlStartTracking();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        colorChangeListener.onColorControlStopTracking();
    }
}