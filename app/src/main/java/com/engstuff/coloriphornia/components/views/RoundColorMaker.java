package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class RoundColorMaker extends View {

    private float x;
    private float y;
    private int  size;

    public RoundColorMaker(Context context) {
        this(context, null);
    }

    public RoundColorMaker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundColorMaker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        // see your later
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = measure(widthMeasureSpec);
        int h = measure(heightMeasureSpec);
        size = Math.min(w, h);
        setMeasuredDimension(size, size);

        calculateCoords();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        c.drawColor(Color.RED);
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) result = 200;
        else result = specSize;
        return result;
    }

    private void calculateCoords() {
        x = size * .5f;
        y = x;
    }
}
























