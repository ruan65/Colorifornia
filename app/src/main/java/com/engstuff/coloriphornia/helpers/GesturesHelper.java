package com.engstuff.coloriphornia.helpers;

import android.view.MotionEvent;
import android.view.View;

public class GesturesHelper {


    public static void performSingleTouch(View v, long x, long y) {

        v.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, x, y, 0));

        v.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, x, y, 0));
    }
}
