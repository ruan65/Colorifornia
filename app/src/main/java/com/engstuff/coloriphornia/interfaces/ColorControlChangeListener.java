package com.engstuff.coloriphornia.interfaces;

public interface ColorControlChangeListener {

    void onColorControlChange();
    void onColorControlChange(int color, int alpha, float[] hsv);
    void onColorControlStartTracking();
    void onColorControlStopTracking();
}
