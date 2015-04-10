package com.engstuff.coloriphornia.components.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.engstuff.coloriphornia.interfaces.RoundColorMakerChangedListener;

import java.lang.Math;
import java.lang.Override;

public class RoundColorMaker extends View implements View.OnTouchListener {

    protected static final int SET_COLOR = 0;
    protected static final int SET_SATUR = 1;
    protected static final int SET_ALPHA = 2;

    private int mode;
    private int mColor;

    private float cx;
    private float cy;
    private int size;

    private float rad_1;
    private float rad_2;
    private float rad_3;
    private float r_centre;

    private float r_sel_c;
    private float r_sel_s;
    private float r_sel_a;

    private Paint p_color = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_satur = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_alpha = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_white = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_handl = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint p_centre = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float deg_sat;
    private float deg_alp;

    private float lc;
    private float lm;
    private float lw;

    private int[] argb = {255, 0, 0, 0};
    private float[] hsv = {0, 1, 1};
    private int alpha;

    private RoundColorMakerChangedListener roundColorMakerChangedListener;

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

        p_centre.setStyle(Paint.Style.FILL_AND_STROKE);

        p_white.setColor(Color.WHITE);
        p_white.setStrokeWidth(2);

        p_handl.setStrokeWidth(5);
        p_handl.setStrokeCap(Paint.Cap.SQUARE);
        p_handl.setColor(Color.WHITE);

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
        cx = size * 0.5f;
        cy = cx;
        lm = size * 0.043f;
        lw = size * 0.035f;
        rad_1 = size * 0.44f;
        r_sel_c = size * 0.39f;
        rad_2 = size * 0.34f;
        r_sel_s = size * 0.29f;
        rad_3 = size * 0.24f;
        r_sel_a = size * 0.19f;
        r_centre = size * 0.18f;

        lc = size * 0.08f;

        p_color.setStrokeWidth(lc);
        p_satur.setStrokeWidth(lc);
        p_alpha.setStrokeWidth(lc);
    }

    private void drawColorPickingCircle(Canvas canvas) {

        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.RED};

        Shader s = new SweepGradient(cx, cy, colors, null);

        p_color.setShader(s);

        canvas.drawCircle(cx, cy, rad_1, p_color);
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
        canvas.drawCircle(cx, cy, rad_2, p_satur);
    }

    private void drawAlphaGradient(Canvas canvas) {

        // Three lines for evaluation of transparency
        canvas.drawCircle(cx, cy, rad_3 - lw, p_white);
        canvas.drawCircle(cx, cy, rad_3, p_white);
        canvas.drawCircle(cx, cy, rad_3 + lw, p_white);

        int[] arrayForShader = {
                mColor,
                Color.argb(0, Color.red(mColor), Color.green(mColor), Color.blue(mColor))
        };

        p_alpha.setShader(new SweepGradient(cx, cy, arrayForShader, null));
        canvas.drawCircle(cx, cy, rad_3, p_alpha);
    }

    private void drawHandles(Canvas c) {

        drawHandle(c, hsv[0], rad_1);
        drawHandle(c, deg_sat, rad_2);
        drawHandle(c, deg_alp, rad_3);
    }

    private void drawHandle(Canvas c, float d, float rad) {

        c.rotate(d, cx, cy);
        c.drawLine(cx + rad + lm, cy, cx + rad - lm, cy, p_handl);
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
        p_centre.setColor(mColor);
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
        p_centre.setColor(mColor);
    }

    private void setAlphaScale(float x, float y) {

        deg_alp = getAngle(x, y);

        argb[0] = (int) (255 - deg_alp / 360 * 255);

        mColor = Color.HSVToColor(argb[0], hsv);

        alpha = Color.alpha(mColor) / 255;

        p_centre.setColor(mColor);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                float a = Math.abs(event.getX() - cx);
                float b = Math.abs(event.getY() - cy);

                float c = (float) Math.sqrt(a * a + b * b);

                if (c > r_sel_c) mode = SET_COLOR;
                else if (c < r_sel_c && c > r_sel_s) mode = SET_SATUR;
                else if (c < r_sel_s && c > r_sel_a) mode = SET_ALPHA;
//                else if (c < r_centre) onColorChangedListener.onDismiss(mColor, alpha);

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
                break;
        }
        invalidate();

        if (roundColorMakerChangedListener != null)
            roundColorMakerChangedListener.onColorChanged(mColor, alpha);

        return true;
    }

    public void setRoundColorMakerChangedListener(RoundColorMakerChangedListener l) {
        this.roundColorMakerChangedListener = l;
    }
}
























