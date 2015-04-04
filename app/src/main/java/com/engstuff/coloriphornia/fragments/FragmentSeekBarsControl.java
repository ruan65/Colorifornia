package com.engstuff.coloriphornia.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.engstuff.coloriphornia.R;

public class FragmentSeekBarsControl extends Fragment implements SeekBar.OnSeekBarChangeListener {

    public interface ColorControlChangeListener {

        void onColorControlChange();
        void onColorControlStartTracking();
        void onColorControlStopTracking();
    }

    public FragmentSeekBarsControl() {}

    private ColorControlChangeListener colorChangeListener;

    private SeekBar sbAlpha, sbRed, sbGreen, sbBlue;
    private int alpha, r, g, b; // alpha, red, green, blue

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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()) {
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
            default:
                break;
        }
        colorChangeListener.onColorControlChange();
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
