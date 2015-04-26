package com.engstuff.coloriphornia.interfaces;

public interface ColorControlChangeListener {

    void onColorControlChange(int progress);
    void onColorControlChange(int progress, int color, int alpha, float[] hsv);
    void onColorControlStartTracking();
    void onColorControlStopTracking();
}
