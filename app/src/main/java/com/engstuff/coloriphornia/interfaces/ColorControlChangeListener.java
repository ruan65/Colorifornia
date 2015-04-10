package com.engstuff.coloriphornia.interfaces;

public interface ColorControlChangeListener {

    void onColorControlChange();
    void onColorControlChange(int color, int alpha);
    void onColorControlStartTracking();
    void onColorControlStopTracking();
}
