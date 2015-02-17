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

        public void onColorControlChange();
    }

    public FragmentSeekBarsControl() {}

    private ColorControlChangeListener colorChangeListener;

    private SeekBar sbAlfa, sbRed, sbGreen, sbBlue;
    private int alpha, r, g, b; // alpha, red, green, blue

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seek_bars_control, container, false);

        sbAlfa = (SeekBar) rootView.findViewById(R.id.sbAlfa);
        sbRed = (SeekBar) rootView.findViewById(R.id.sbRed);
        sbGreen = (SeekBar) rootView.findViewById(R.id.sbGreen);
        sbBlue = (SeekBar) rootView.findViewById(R.id.sbBlue);

        sbAlfa.setOnSeekBarChangeListener(this);
        sbRed.setOnSeekBarChangeListener(this);
        sbGreen.setOnSeekBarChangeListener(this);
        sbBlue.setOnSeekBarChangeListener(this);

        alpha = getResources().getInteger(R.integer.sbMax);
        r = g = b = getResources().getInteger(R.integer.sbProgress);

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
            case R.id.sbAlfa:
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

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

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
