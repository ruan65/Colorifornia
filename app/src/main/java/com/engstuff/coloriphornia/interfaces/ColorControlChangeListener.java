package com.engstuff.coloriphornia.interfaces;

public interface ColorControlChangeListener {

    void onColorControlChange(int progress, int seekId);
    void onColorControlChange(int progress, int color, int alpha, float[] hsv);
    void onColorControlStartTracking();
    void onColorControlStopTracking();
}
