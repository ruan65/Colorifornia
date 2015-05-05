package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.engstuff.coloriphornia.interfaces.ColorControlChangeListener;

public class RoundColorMaker extends View implements View.OnTouchListener {

    protected static final int SET_COLOR = 0;
    protected static final int SET_SATUR = 1;
    protected static final int SET_ALPHA = 2;

    private int mode = -1;
    private int mColor;

    private float cx, cy;
    private int size;

    private float r1, r2, r3;

    private Paint p_color = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_satur = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_alpha = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_white = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_handle = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float deg_sat, deg_alp;

    private float halfHandle;

    private int[] argb = {255, 0, 0, 0};
    private float[] hsv = {0, 1, 1};

    private ColorControlChangeListener colorControlChangeListener;
    private float stroke;

    public RoundColorMaker(Context context) {
        this(context, null);
    }

    public RoundColorMaker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundColorMaker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        p_color.setStyle(Paint.Style.STROKE);
        p_satur.setStyle(Paint.Style.STROKE);
        p_white.setStyle(Paint.Style.STROKE);
        p_alpha.setStyle(Paint.Style.STROKE);

        p_white.setColor(Color.WHITE);
        p_white.setStrokeWidth(3);
        p_white.setPathEffect(new DashPathEffect(new float[]{3, 5}, 0));

        p_handle.setStrokeWidth(5);
        p_handle.setStrokeCap(Paint.Cap.SQUARE);
        p_handle.setColor(Color.WHITE);

        setFocusable(true);
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = measure(widthMeasureSpec);
        int h = measure(heightMeasureSpec);

        size = Math.min(w, h);
        setMeasuredDimension(size, size);

        calculateThenSetSizesAndCoords();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        drawColorPickingCircle(c);
        drawSaturationGradient(c);
        drawAlphaGradient(c);
        drawHandles(c);
    }

    private void calculateThenSetSizesAndCoords() {

        cy = cx = size * 0.5f;

        stroke = size * 0.11f;

        halfHandle = stroke / 1.85f;

        r1 = size * 0.43f;

        r2 = size * 0.294f;

        r3 = size * 0.159f;

        p_color.setStrokeWidth(stroke);
        p_satur.setStrokeWidth(stroke);
        p_alpha.setStrokeWidth(stroke);
    }

    private void drawColorPickingCircle(Canvas canvas) {

        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.RED};

        Shader s = new SweepGradient(cx, cy, colors, null);

        p_color.setShader(s);

        canvas.drawCircle(cx, cy, r1, p_color);
    }

    private void drawSaturationGradient(Canvas canvas) {

        float angle = hsv[0];

        int[] sar = {
                
                Color.HSVToColor(new float[]{angle, 1, 0}),
                Color.HSVToColor(new float[]{angle, 1, 1}),
                Color.HSVToColor(new float[]{angle, 0, 1}),
                Color.HSVToColor(new float[]{angle, 0, .5f}),
                Color.HSVToColor(new float[]{angle, 1, 0})
        };

        p_satur.setShader(new SweepGradient(cx, cy, sar, null));
        canvas.drawCircle(cx, cy, r2, p_satur);
    }

    private void drawAlphaGradient(Canvas canvas) {

        canvas.drawCircle(cx, cy, r3, p_white);

        int solidColor = mColor | 0xff000000;

        int[] arrayForShader = {
                solidColor,
                Color.argb(0, Color.red(solidColor), Color.green(solidColor), Color.blue(solidColor))
        };

        p_alpha.setShader(new SweepGradient(cx, cy, arrayForShader, null));
        canvas.drawCircle(cx, cy, r3, p_alpha);
    }

    private void drawHandles(Canvas c) {

        drawHandle(c, hsv[0], r1);
        drawHandle(c, deg_sat, r2);
        drawHandle(c, deg_alp, r3);
    }

    private void drawHandle(Canvas c, float d, float rad) {

        c.rotate(d, cx, cy);
        c.drawLine(cx + rad + halfHandle, cy, cx + rad - halfHandle, cy, p_handle);
        c.rotate(-d, cx, cy);
    }

    private int measure(int measureSpec) {

        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) result = 200;
        else result = specSize;

        return result;
    }

    private float getAngle(float x, float y) {

        float deg = x == 0 ? 0 : y / x;

        deg = (float) Math.toDegrees(Math.atan(deg));

        return x < 0 ? deg + 180 : x > 0 && y < 0 ? deg + 360 : deg;
    }

    private void setColScale(float x, float y) {

        hsv[0] = getAngle(x, y);

        mColor = Color.HSVToColor(argb[0], hsv);
    }

    private void setSatScale(float x, float y) {

        deg_sat = getAngle(x, y);

        if (deg_sat < 90) {
            hsv[1] = 1;
            hsv[2] = deg_sat / 90;
        } else if (deg_sat >= 90 && deg_sat <= 180) {
            hsv[1] = 1 - (deg_sat - 90) / 90;
            hsv[2] = 1;
        } else {
            hsv[1] = 0;
            hsv[2] = 1 - (deg_sat - 180) / 180;
        }
        mColor = Color.HSVToColor(argb[0], hsv);
    }

    private void setAlphaScale(float x, float y) {

        deg_alp = getAngle(x, y);

        argb[0] = (int) (255 - deg_alp / 360 * 255);

        mColor = Color.HSVToColor(argb[0], hsv);
    }

    private float getSatDegree() {
        return hsv[1] == 1f ? hsv[2] * 90 : hsv[2] == 1f ?
                (1 - hsv[1]) * 90 + 90 : (1 - hsv[2]) * 180 + 180;
    }

    private float getAlphaDegree() {
        return (255 - argb[0]) * 360 / 255;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                float a = Math.abs(event.getX() - cx);
                float b = Math.abs(event.getY() - cy);

                float c = (float) Math.sqrt(a * a + b * b);

                float delta = stroke / 2;

                if (c > r1 - delta && c < r1 + delta) mode = SET_COLOR;
                else if (c < r1 - delta && c > r2 - delta) mode = SET_SATUR;
                else if (c < r2 - delta && c > r3 - delta) mode = SET_ALPHA;

                colorControlChangeListener.onColorControlStartTracking();
                break;

            case MotionEvent.ACTION_UP:

                mode = -1; // no actions anymore
                colorControlChangeListener.onColorControlStopTracking();
                break;

            case MotionEvent.ACTION_MOVE:

                float x = event.getX() - cx;
                float y = event.getY() - cy;

                switch (mode) {

                    case SET_COLOR:
                        setColScale(x, y);
                        break;

                    case SET_SATUR:

                        setSatScale(x, y);
                        break;

                    case SET_ALPHA:

                        setAlphaScale(x, y);
                        break;
                }
                colorControlChangeListener.onColorControlChange(0, mColor, argb[0], hsv);
                break;
        }
        invalidate();

        return true;
    }

    public void setColorAndControls(int... p) { // alpha, r, g, b

        argb[0] = p[0];
        argb[1] = p[1];
        argb[2] = p[2];
        argb[3] = p[3];

        proceedColorParamSetting(argb);
    }

    private void proceedColorParamSetting(int... p) {

        mColor = Color.argb(p[0], p[1], p[2], p[3]);
        Color.colorToHSV(mColor, hsv);

        deg_sat = getSatDegree();
        deg_alp = getAlphaDegree();

        colorControlChangeListener.onColorControlChange(0, mColor, p[0], hsv);
    }

    public void resetAlphaAndSatur() {

        setAlphaScale(0, 0);
        setSatScale(1, Float.MAX_VALUE);

        colorControlChangeListener.onColorControlChange(0, mColor, argb[0], hsv);
        colorControlChangeListener.onColorControlStopTracking();

        invalidate();
    }


    public void setColorControlChangeListener(ColorControlChangeListener l) {
        this.colorControlChangeListener = l;
    }
}
























