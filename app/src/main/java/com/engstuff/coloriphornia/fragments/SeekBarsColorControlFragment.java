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
    SeekBarButton minus_red;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seek_bars_control, container, false);

        minus_red = (SeekBarButton) rootView.findViewById(R.id.minus_red);
        sbRed = (SeekBar) rootView.findViewById(R.id.sbRed);
        minus_red.init(sbRed, colorChangeListener, false);

        ButterKnife.inject(this, rootView);

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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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

//    @OnClick(R.id.minus_red)
//    public void dR() {
//        stepSeekBar(sbRed, false);
//    }

    @OnClick(R.id.minus_green)
    public void dG() {
        stepSeekBar(sbGreen, false);
    }

    @OnClick(R.id.minus_blue)
    public void dB() {
        stepSeekBar(sbBlue, false);
    }

    @OnClick(R.id.minus_alpha)
    public void dA() {
        stepSeekBar(sbAlpha, false);
    }

    @OnClick(R.id.plus_red)
    public void iR() {
        stepSeekBar(sbRed, true);
    }

    @OnClick(R.id.plus_green)
    public void iG() {
        stepSeekBar(sbGreen, true);
    }

    @OnClick(R.id.plus_blue)
    public void iB() {
        stepSeekBar(sbBlue, true);
    }

    @OnClick(R.id.plus_alpha)
    public void iA() {
        stepSeekBar(sbAlpha, true);
    }

//    @OnLongClick(R.id.minus_red)
//    public boolean rmGo() {
//
//        return true;
//    }

//    @OnTouch(R.id.minus_red)
//    public boolean rmPressed(MotionEvent ev) {
//
//        switch (ev.getAction()) {
//
//            case MotionEvent.ACTION_MOVE:
//                Log.d("ml", "Moved................");
//                break;
//
//            case MotionEvent.ACTION_UP:
//                Log.d("ml", "Up........................");
//                break;
//        }
//        return false;
//    }



    private void stepSeekBar(SeekBar sb, boolean inc) {

        colorChangeListener.onColorControlStartTracking();

        int prs = sb.getProgress();

        sb.setProgress(inc ? (prs < 255 ?  ++prs : prs) : (prs > 0 ? --prs : prs));

        colorChangeListener.onColorControlChange(prs, sb.getId());
        colorChangeListener.onColorControlStopTracking();
    }
}