package com.engstuff.coloriphornia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.R;

public class SeekBarsColorControlFragment extends ColorControlAbstractFragment
        implements SeekBar.OnSeekBarChangeListener {

    private SeekBar sbAlpha, sbRed, sbGreen, sbBlue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seek_bars_control, container, false);

        sbAlpha = (SeekBar) rootView.findViewById(R.id.sbAlpha);
        sbRed = (SeekBar) rootView.findViewById(R.id.sbRed);
        sbGreen = (SeekBar) rootView.findViewById(R.id.sbGreen);
        sbBlue = (SeekBar) rootView.findViewById(R.id.sbBlue);

        sbAlpha.setOnSeekBarChangeListener(this);
        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);

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
